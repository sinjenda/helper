import atexit

import numpy as np
import HandTrackingModule as htm
import cv2
import mouse
import speech_recognition as sr
import time


def main():
    r = sr.Recognizer()

    print(sr.Microphone.list_microphone_names())
    mic = sr.Microphone()
    print(isinstance(mic, sr.AudioSource))
    with mic as source:
        r.adjust_for_ambient_noise(source)
        r.pause_threshold = 1
        while True:
            print("listening")
            audio2 = r.listen(source, None, None, None)
            try:
                my_text = r.recognize_google(audio2)
                print (my_text)
            except sr.UnknownValueError:
                continue
            my_text.lower()
            if my_text == "ok":
                virtual()
            if my_text == "exit":
                exit(0)


def virtual():
    global x1, y1
    #wCam, hCam = 640, 480
    wCam,hCam=1280*2,720*2
    frameR = 100  # Frame Reduction
    smoothening = 7

    pTime = 0
    plocX, plocY = 0, 0
    clocX, clocY = 0, 0

    cap = cv2.VideoCapture(0)
    cap.set(3, 1280)
    cap.set(4, 720)
    detector = htm.handDetector(maxHands=1)
    wScr, hScr = 1920, 1080
    # print(wScr, hScr)
    while True:
        # 1. Find hand Landmarks
        success, img = cap.read()
        img=cv2.resize(img,(wCam,hCam))
        img = detector.findHands(img)
        lmList, bbox = detector.findPosition(img)
        # 2. Get the tip of the index and middle fingers
        if len(lmList) != 0:
            x1, y1 = lmList[8][1:]
            x2, y2 = lmList[12][1:]
            # print(x1, y1, x2, y2)

        # 3. Check which fingers are up
        fingers = detector.fingersUp()
        # print(fingers)
        cv2.rectangle(img, (frameR, frameR), (wCam - frameR, hCam - frameR),
                      (255, 0, 255), 2)
        # 4. Only Index Finger : Moving Mode
        if fingers[1] == 1 and fingers[2] == 0:
            # 5. Convert Coordinates
            x3 = np.interp(x1, (frameR, wCam - frameR), (0, wScr))
            y3 = np.interp(y1, (frameR, hCam - frameR), (0, hScr))
            # 6. Smoothen Values
            clocX = plocX + (x3 - plocX) / smoothening
            clocY = plocY + (y3 - plocY) / smoothening

            # 7. Move Mouse
            mouse.move(wScr - clocX, clocY)
            cv2.circle(img, (x1, y1), 15, (255, 0, 255), cv2.FILLED)
            plocX, plocY = clocX, clocY

        # 8. Both Index and middle fingers are up : Clicking Mode
        if fingers[1] == 1 and fingers[2] == 1:
            # 9. Find distance between fingers
            length, img, lineInfo = detector.findDistance(8, 12, img)
            # print(length)
            # 10. Click mouse if distance short
            if length < 40:
                cv2.circle(img, (lineInfo[4], lineInfo[5]), 15, (0, 255, 0), cv2.FILLED)
                mouse.click()

                # 11. Frame Rate
        cTime = time.time()
        fps = 1 / (cTime - pTime)
        pTime = cTime
        img = cv2.flip(img, 1)
        cv2.putText(img, str(int(fps)), (20, 50), cv2.FONT_HERSHEY_PLAIN, 3, (255, 0, 0), 3)
        # 12. Display
        img=cv2.resize(img,(int((wCam/2)/2),int((hCam/2)/2)))
        cv2.imshow("Image", img)
        cv2.waitKey(1)


atexit.register(main)
if __name__ == "__main__":
    main()
