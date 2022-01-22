import math
import time
from ctypes import cast, POINTER
import mouse
import cv2
import win32api
import threading
from pynput import keyboard
from comtypes import CLSCTX_ALL
from pycaw.pycaw import AudioUtilities, IAudioEndpointVolume

import HandTrackingModule as htm

wCam, hCam = 1280, 720
cap = cv2.VideoCapture(0)
# cv2.namedWindow("hhh", cv2.WND_PROP_FULLSCREEN)
# cv2.setWindowProperty("hhh", cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_FULLSCREEN)
cap.set(3, wCam)
cap.set(4, hCam)
pTime = 0
cTime = 0
vol = 0
volBar = 400

detector = htm.handDetector(detectionCon=0.3)
devices = AudioUtilities.GetSpeakers()
interface = devices.Activate(IAudioEndpointVolume._iid_, CLSCTX_ALL, None)
volume = cast(interface, POINTER(IAudioEndpointVolume))
volRange = volume.GetVolumeRange()
minVol = volRange[0]
maxVol = volRange[1]


class data(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.length = 400


thr = data()
# thr.start()

while True:
    success, img = cap.read()

    img = cv2.resize(img, (1920, 1080))
    img = cv2.flip(img, 1)
    cTime = time.time()
    fps = 1 / (cTime - pTime)
    pTime = cTime
    if not success:
        continue
    img = detector.findHands(img)

    lmList = detector.findPosition(img, draw=False)
    # print(lmList)
    if len(lmList) != 0:
        x1, y1 = lmList[4][1], lmList[4][2]
        x2, y2 = lmList[8][1], lmList[8][2]
        # cv2.circle(img, (x1, y1), 15, (255, 0, 255), cv2.FILLED)
        cv2.circle(img, (x2, y2), 15, (255, 0, 255), cv2.FILLED)
        # cv2.line(img, (x1, y1), (x2, y2), (255, 0, 255), 3)
        cx, cy = (x1 + x2) // 2, (y1 + y2) // 2
        # cv2.circle(img, (cx, cy), 15, (255, 0, 255), cv2.FILLED)
        win32api.SetCursorPos((x2, y2))
        length2 = math.hypot(lmList[20][1] - lmList[0][1], lmList[20][2] - lmList[0][2])
        thr.length = length2
    cv2.putText(img, f'FPS: {int(fps)}', (40, 70), cv2.FONT_HERSHEY_COMPLEX, 1, (255, 0, 255), 3)
    cv2.imshow("Img", img)
    if cv2.waitKey(1) & 0xFF == ord('s'):
        break
cap.release()
cv2.destroyAllWindows()
print("clicked")
