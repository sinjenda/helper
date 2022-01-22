import cv2

cap = cv2.VideoCapture(0)
x,y=640, 480
cap.set(3, x)
cap.set(4, y)
success,img=cap.read()
cv2.imshow("Image",img)
img=cv2.resize(img,(x*5,y*5))
img=cv2.resize(img,(x,y))
cv2.imshow("resized",img)
cv2.waitKey(-5)
