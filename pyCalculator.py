import math

MASS = 10.0      #mass
SPEED_INC=0.09 
MAX_FORCE = 1     #the max force exerted per step by the drone
MAX_YAW_SPEED = 0.05
MAX_TOTAL_FORCE = 5000.0
SHELL_BUFF=50.0
MIN_ABS_VEL= 0.05
xForce=0.0
zForce=0.0
pXForce = 0
pZForce = 0
rXForce = 0
rZForce = 0
xVel=0.0
zVel=0.0
xPos=0.0
zPos=0.0
timeStep=0.0
drag=0.999

MAX_VEL = 125

countVal = 50
autoCount = countVal
  
canFly=False

objs=[]

MAX_THROTTLE = 23000
MAX_YAW = 26000
MAX_PITCH = 23000
MAX_ROLL = 25000

cAngle = 0

class pyCalculator:

  def getPercent(self,val):
    MAX = 0.8
    MIN = 0.16

    percent = MIN if val < MIN else MAX if val > MAX else val
    percent = ((percent-MIN)/(MAX-MIN))
    percent = (percent-0.5)/0.5
    return percent if abs(percent)>0.1 else 0.0

  def getAccel(self, comp):
    return (comp.x()/MASS,comp.y()/MASS)

  def getVel(self, comp):
    accelVector = getAccel(comp)
    global xVel
    global zVel
    nXVel = (xVel + accelVector[0]*timeStep)*drag
    nZVel = (ZVel + accelVector[1]*timeStep)*drag
    xVel = -MAX_VEL if nXVel <= -MAX_VEL else MAX_VEL if nXVel >= MAX_VEL else 0 if abs(nXVel)<MIN_ABS_VEL else nXVel
    zVel = -MAX_VEL if nYVel <= -MAX_VEL else MAX_VEL if nYVel >= MAX_VEL else 0 if abs(nYVel)<MIN_ABS_VEL else nYVel
    return (xVel,zVel)

  def getPos(self, comp):
    velVector = getVel(comp)
    global xPos
    global zPos
    nXPos = xPos + velVector[0]*timeStep
    nYPos = zPos - velVector[1]*timeStep
    xPos = nXPos
    zPos = nYPos
    return (nXPos,nYPos)

  def analyze(self, xComp, zComp):
    output = getPos((xComp,zComp))
    xComp*=drag
    zComp*=drag
    #myDrone.setLocation(output[0],output[1])

  def getUnitVectorFrom(self, obj):
    if (obj == null): 
      return (0,0)
    objPos = obj.getPosition()
    dx = xPos - objPos[0]
    dy = yPos - objPos[1]
    mag = mySqrt(dx,dy)
    return (dx/mag,dy/mag)

  def mySqrt(self, x, z):
    return sqrt(pow(x,2)+pow(z,2))

  def getAngle(x, y):
    #Vector vel = new Vector(xVel,yVel)
    return degrees(atan2(y,x))

  def __init__(self,x,z,dx,dz,angle):
    global xPos
    global zPos
    global xVel
    global zVel
    global cAngle
    xPos = x
    zPos = z
    xVel = dx
    zVel = dz
    cAngle = angle + 90
 
  def run(self):
    for obj in objs:
      #correct this !! 
      #prevDist = obj.distance
      myDist = 2 #obj.getDistanceFrom(xPos,yPos)
      

      stayAwayFromMe = 1 #SHELL_BUFF+abs((MAX_VEL*mySqrt(xVel,yVel))/(2.5*MAX_TOTAL_FORCE/MASS))
      if( (myDist < stayAwayFromMe and prevDist > myDist) and ((autoCount!=countVal) or (myDist < stayAwayFromMe and prevDist > myDist) or (myDist < SHELL_BUFF))):
        pXForce = 0
        pZForce = 0
        rXForce = 0
        rZForce = 0
        flyDrone(false, obj)
        myDrone.auto()
        if(autoCount == 0):
          obj.close(false)
          autoCount = countVal
        else:
          autoCount-=1
      
    if(canFly and autoCount == countVal):
      flyDrone(true, null)
  
  def flyDrone(self, manual, obj, t, y, p, r, cAngle):
    #cAngle = myDrone.getCAngle()
    pForce=0
    rForce=0
    
    global pXForce
    global pZForce
    global rXForce
    global rZForce
    global xForce
    global zForce
    global xVel
    global zVel

    if(manual):
      pForce = MAX_FORCE*getPercent(p)
      rForce = MAX_FORCE*getPercent(r)
      
      pXForce += (pForce*cos(cAngle))
      pZForce += (pForce*sin(cAngle))
      
      rXForce += (rForce*cos(cAngle-90.0))
      rZForce += (rForce*sin(cAngle-90.0))
      
      xForce = pXForce + rXForce
      zForce = pZForce + rZForce
    
    else:
      #if unaltered then there is only repulsion as the vector is from the object pointing in the direction of the drone
      unitV = getUnitVectorFrom(obj)
      
      #This method style allows for a smoother avoidance rather than just straight repulsion
      """
      start
       double an1 = 180-getAngle(unitV.x(),unitV.y())
       double an2 = getAngle(xVel,yVel)
       unitV.makeOrthogonal(an1 < 0 ? an1 + 360 : an1, an2 < 0 ? an2 + 360 : an2)
       //end
      """
      
      if(mySqrt(xVel,zVel) > 0.5):
        unitV[0] = unitV[0]*(mySqrt(xVel,yVel))
        unitV[1] = unitV[1]*(mySqrt(xVel,yVel))
      else:
        unitV[0] = unitV[0]*(2)
        unitV[1] = unitV[1]*(2)

      xForce += unitV[0]
      zForce -= unitV[1]
    
    xForce = MAX_TOTAL_FORCE if xForce > MAX_TOTAL_FORCE else -MAX_TOTAL_FORCE if xForce < -MAX_TOTAL_FORCE else xForce
    zForce = MAX_TOTAL_FORCE if zForce > MAX_TOTAL_FORCE else -MAX_TOTAL_FORCE if zForce < -MAX_TOTAL_FORCE else zForce
    
    if(pForce==0):
      pXForce = 0
      pZForce = 0
    
    if(rForce == 0):
      rXForce = 0
      rZForce = 0
    
    analyze(xForce, zForce)
    #setYaw()
    #setAltitude()  
  
  """private double getThrottlePercent(){
    double throttle = cont.getThrottle()
    double val = throttle < -MAX_THROTTLE ? -MAX_THROTTLE : throttle > MAX_THROTTLE ? MAX_THROTTLE : throttle
    return (val+MAX_THROTTLE)/(2*MAX_THROTTLE)
  }
  
  private double getYawPercent(){
    double yaw = -cont.getYaw()
    double yawPercent = yaw < -MAX_YAW ? -MAX_YAW : yaw > MAX_YAW ? MAX_YAW : yaw
    return (yawPercent/MAX_YAW)
  }
  
  private double getRollPercent(){
    double roll = -cont.getRoll()
    double rollPercent = roll < -MAX_ROLL ? -MAX_ROLL : roll > MAX_ROLL ? MAX_ROLL : roll
    return (rollPercent/MAX_ROLL)
  }
  
  private double getPitchPercent(){
    double pitch = cont.getPitch() 
    double pitchPercent = pitch < -MAX_PITCH ? -MAX_PITCH : pitch > MAX_PITCH ? MAX_PITCH : pitch
    return (pitchPercent/MAX_PITCH)
  }
  
  private void setYaw(){
    myDrone.setYawVal(-MAX_YAW_SPEED*getYawPercent(),Math.abs(getYawPercent()) < 0.01)
  }
  
  public void setAltitude(){
    double throttle = getThrottlePercent()    
    double sf = D_ALT
    if(altitude*D_ALT > MIN_SIZE && altitude*D_ALT < MAX_SIZE){
      sf = (altitude > throttle*(MAX_SIZE-MIN_SIZE)+MIN_SIZE) ? sf : 1/sf
      altitude*=sf
      myDrone.scale(sf)
    }
  }"""
  
  def startFlight():
    global canFly
    canFly = True