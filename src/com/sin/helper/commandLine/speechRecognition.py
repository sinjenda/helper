import speech_recognition as sr

r = sr.Recognizer()

print(sr.Microphone.list_microphone_names())
mic = sr.Microphone()
print(isinstance(mic, sr.AudioSource))
with mic as source:
    r.adjust_for_ambient_noise(source)
    r.pause_threshold = 1
    while True:
        audio2 = r.listen(source, None, None, None)
        MyText = r.recognize_google(audio2)
        MyText.lower()
        print(MyText)
