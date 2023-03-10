import csv
import json
import matplotlib.pyplot as plt
import matplotlib
import math
import numpy as np
# Feel free to add anything else you need here

# Read the event log into an array of dictionaries
events = []
with open('0021500495.csv', mode='r') as event_csv:
    event_reader = csv.DictReader(event_csv)
    line_count = 0
    for row in event_reader:
        events.append(dict(row))

# Read in the SportVU tracking data
sportvu = []
with open('0021500495.json', mode='r') as sportvu_json:  
    sportvu = json.load(sportvu_json)

events = sportvu['events']


homeBasketX = 5.25
visitorBasketX = 94 - 5.25
basketY = 24.605
basketZ = 10


prevWT = 0
prevST = 100000000
time = 720

ballCoords = []
numMoments = 0

shot_times = np.array([]) # Between 0 and 2880
shot_facts = np.array([]) # Scaled between 0 and 10

maxDist = 0
minDist = 10000000000

for event in events:
    moments = event['moments']
    for moment in moments:
        #Count number of moments

        #Time left in quarter
        ST = moment[2]
        #World time
        WT = moment[1]
        #Ball coordinates
        ballData = moment[5][0]
        x = ballData[2]
        y = ballData[3]
        z = ballData[4]
        quarter = moment[0]
        
        if (WT > prevWT):
            numMoments += 1
            #Store ball coordaintes coordinate and quarter time
            ballCoords.append((x, y, z, ST))
            #print("X: " + str(x) + "\t Y: " + str(y) + "\t Z: " + str(z))
            prevWT = WT
            #Check if ball close to basket
            if(abs(x - homeBasketX) < 1 and abs(y - basketY) < 1 and abs(z - basketZ) < 1):
                #Check if a unique shot
                if(prevST - ST  > .15):
                    #Look back at prev Z's to find when shot was taken

                    #z that we already looked at in array
                    for i in range(numMoments - 1, 0, -1):
                        
                        #Find when z was most recently deccreasing
                        if 9 > ballCoords[i][2]:
                            time =  quarter * 720 -  ballCoords[i][3]
                         

                            #Pythag theorem
                            
                            xDist = abs(ballCoords[i][0] - homeBasketX)
                            yDist = abs(ballCoords[i][1] - basketY)
                            timeyy = str(int(ballCoords[i][3] / 60)) + ":" + str(int(ballCoords[i][3] % 60))
                            print("Time : " + str(timeyy)  + "\t Dist "  + str(totalDist))
                            # print("XDist: " + str(xDist) + "\t YDist: " + str(yDist))
                            totalDist = math.sqrt((xDist ** 2) + (yDist ** 2))
                            if(totalDist < 40):
                                if (totalDist > maxDist):
                                    maxDist = totalDist
                                if (totalDist < minDist):
                                    minDist = totalDist

                                shot_facts = np.insert(shot_facts, shot_facts.size, totalDist)
                                shot_times = np.insert(shot_times, shot_times.size, time)
                                break
                            else:
                                break
                prevST = ST
            if(abs(x - visitorBasketX) < 1 and abs(y - basketY) < 1 and (z > basketZ or abs(z - basketZ) < 1)):
                if(prevST - ST  > .15):
                    #Look back at prev Z's to find when shot was taken

                    #z that we already looked at in array

                    for i in range(numMoments - 2, 0, -1):

                        #Find when z was most recently deccreasing
                        if 9 > ballCoords[i][2]:
                            time =  quarter * 720 -  ballCoords[i][3]
                            

                            #Pythag theorem
                            xDist = abs(ballCoords[i][0] - visitorBasketX)
                            yDist = abs(ballCoords[i][1] - basketY)
                            totalDist = math.sqrt((xDist ** 2) + (yDist ** 2))
                            if(totalDist < 40):
                                if (totalDist > maxDist):
                                    maxDist = totalDist
                                if (totalDist < minDist):
                                    minDist = totalDist
                                timeyy = str(int(ballCoords[i][3] / 60)) + ":" + str(int(ballCoords[i][3] % 60))
                                print("Time : " + str(timeyy)  + "\t Dist "  + str(totalDist))
                                shot_facts = np.insert(shot_facts, shot_facts.size, totalDist)
                                shot_times = np.insert(shot_times, shot_times.size, time)
                                break
                            else:
                                break
                prevST = ST
                

#Normalize shot distances to be between 0 and 10            
for i in range(0, shot_facts.size):
    print("shot distance " + str(shot_facts[i]))
    shot_facts[i] = ((shot_facts[i] - minDist) / (maxDist - minDist)) * 10

print(shot_times.size)
# YOUR SOLUTION GOES HERE
# These are the two arrays that you need to populate with actual data






# This code creates the timeline display from the shot_times
# and shot_facts arrays.
# DO NOT MODIFY THIS CODE APART FROM THE SHOT FACT LABEL

fig, ax = plt.subplots(figsize=(12,3))
fig.canvas.set_window_title('Shot Timeline')

plt.scatter(shot_times, np.full_like(shot_times, 0), marker='o', s=50, color='royalblue', edgecolors='black', zorder=3, label='shot')
plt.bar(shot_times, shot_facts, bottom=2, color='royalblue', edgecolor='black', width=5, label='shot distance') # <- This is the label you can modify

ax.spines['bottom'].set_position('zero')
ax.spines['top'].set_color('none')
ax.spines['right'].set_color('none')
ax.spines['left'].set_color('none')
ax.tick_params(axis='x', length=20)
ax.xaxis.set_major_locator(matplotlib.ticker.FixedLocator([0,720,1440,2160,2880])) 
ax.set_yticks([])

_, xmax = ax.get_xlim()
ymin, ymax = ax.get_ylim()
ax.set_xlim(-15, xmax)
ax.set_ylim(ymin, ymax+5)
ax.text(xmax, 2, "time", ha='right', va='top', size=10)
plt.legend(ncol=5, loc='upper left')

plt.tight_layout()
plt.show()

plt.savefig("Shot_Timeline.png")
