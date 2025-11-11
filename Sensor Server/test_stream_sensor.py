import sys
import time
import grpc
import random
from datetime import datetime

import weather_pb2 as pb
import weather_pb2_grpc as pb_grpc

def readings_generator():
    # توليد قيم عشوائية لكل قراءة
    for i in range(10):  # 10 قراءات
        # قيم عشوائية واقعية للطقس
        temperature = random.uniform(-10, 45)    # من -10 إلى 45 مئوية
        humidity = random.uniform(20, 95)        # من 20% إلى 95% رطوبة
        pressure = random.uniform(980, 1040)     # من 980 إلى 1040 هيكتوباسكال
        
        r = pb.SensorReading(
            sensor_id=f"python-sensor-{random.randint(1000, 9999)}",
            temperature=round(temperature, 2),
            humidity=round(humidity, 2),
            pressure=round(pressure, 2),
            ts=int(time.time() * 1000),
        )
        
        current_time = datetime.now().strftime("%H:%M:%S")
        print(f"[{current_time}] 📤 إرسال قراءة #{i+1}:")
        print(f"   🆔 المستشعر: {r.sensor_id}")
        print(f"   🌡️  الحرارة: {r.temperature:.2f}°C")
        print(f"   💧 الرطوبة: {r.humidity:.2f}%")
        print(f"   📊 الضغط: {r.pressure:.2f} hPa")
        print("   " + "─" * 30)
        
        yield r
        time.sleep(1)  # انتظر ثانية بين كل قراءة

def main():
    host = sys.argv[1] if len(sys.argv) > 1 else "192.168.96.202"  # تأكد من العنوان
    port = int(sys.argv[2]) if len(sys.argv) > 2 else 50052
    target = f"{host}:{port}"
    
    print("=" * 50)
    print("🌤️  عميل مستشعر الطقس - بيانات عشوائية")
    print("=" * 50)
    print(f"📍 الهدف: {target}")
    print(f"🔄 إرسال 10 قراءات عشوائية...")
    print("=" * 50)
    
    # إضافة metadata للتوثيق
    metadata = [('authorization', 'python-token-12345')]
    
    try:
        with grpc.insecure_channel(target) as channel:
            stub = pb_grpc.SensorServiceStub(channel)
            # إرسال مع metadata
            ack = stub.StreamSensorData(readings_generator(), metadata=metadata)
            print(f"✅ تم الاستلام: ok={ack.ok}, msg='{ack.msg}'")
    except grpc.RpcError as e:
        print(f"❌ خطأ في الاتصال: {e}")
    except Exception as e:
        print(f"❌ خطأ غير متوقع: {e}")

if __name__ == '__main__':
    main()