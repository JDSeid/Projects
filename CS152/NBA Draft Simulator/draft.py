import csv
import math
import numpy
import matplotlib.pyplot as plt

# Read the player database into an array of dictionaries


#Player -> (Total VORP, Seasons Played)
players = {}

with open('playerDB.csv', mode='r') as player_csv:
    player_reader = csv.DictReader(player_csv)
    line_count = 0
    for row in player_reader:
        if(row["VORP"] == ''):
            continue
        currPlayer = row["Player"]
        if(not currPlayer in players): 
            #If the player isn't in the dictionary, add them

            players[currPlayer] = [float(row["VORP"]), 1]
            #Store player vorp, and number of seasons played
        else :
            players[currPlayer][0] += float(row["VORP"])
            players[currPlayer][1] += 1

#Change total VORP to average VORP by dividing by number of seasons played
for player in players.keys():
        players[player][0] /= players[player][1]


# Read the draft database into an array of dictionaries

#Pick number -> Total VORP, Valid Seasons
picks = {}
with open('draftDB.csv', mode='r') as draft_csv:
    draft_reader = csv.DictReader(draft_csv)
    line_count = 0
    for row in draft_reader:
        currPlayer = row["namePlayer"].replace("*", "")
        pickNumber = int(row["numberPickOverall"])
        #If the player is found in our player database, and the pick is a valid number
        if (currPlayer in players.keys() and pickNumber > 0 and pickNumber <= 60) :
            #If the pick is not already in the draft database, add them
            if (not pickNumber in picks) :
                picks[pickNumber] = [players[currPlayer][0], 1]

            #If the pick is already in the database, add the picked players' VORP, and inc num seasons 
            else:
                picks[pickNumber][0] +=  players[currPlayer][0]
                picks[pickNumber][1] += 1

#Calculate average VORP for each pick
numPicks = list(picks.keys())
numPicks.sort()
vorps = [0] * 60
for pickNumber in numPicks:
    vorps[pickNumber - 1] = picks[pickNumber][0] / picks[pickNumber][1]
    

#Normalize the picks to take values bewteen 0 and 100
normalized = [0] * 60
for i in numPicks:
    normalized[i - 1] = (vorps[i - 1] - min(vorps))/ (max(vorps) - min(vorps)) * 100
    

#Logistic regression
x = numpy.array(numPicks)
y = numpy.array(normalized)

eq = numpy.polyfit(numpy.log(x), y, 1)
A = eq[0]
B = eq[1]

fit = A * numpy.log(x) + B

# plt.scatter(x, y)
# plt.plot(x, fit, color="orange", label = str(round(A, 4)) + "log(x) + " + str(round(B, 4)))
# plt.xlabel("Pick Number")
# plt.ylabel("Pick Value")
# plt.title("Draft Pick Trade Value")
# plt.legend()

# plt.show()

        

# Get the draft picks to give/receive from the user
# You can assume that this input will be entered as expected
# DO NOT CHANGE THESE PROMPTS

print("\nSelect the picks to be traded away and the picks to be received in return.")
print("For each entry, provide 1 or more pick numbers from 1-60 as a comma-separated list.")
print("As an example, to trade the 1st, 3rd, and 25th pick you would enter: 1, 3, 25.\n")
give_str = input("Picks to give away: ")
receive_str = input("Picks to receive: ")

#convert user input to an array of ints

give_picks = list(map(int, give_str.split(',')))
receive_picks = list(map(int, receive_str.split(',')))

# Success indicator that you will need to update based on your trade analysis
success = True

success = (sum(give_picks) <= sum(receive_picks))

# Print feeback on trade
# DO NOT CHANGE THESE OUTPUT MESSAGES
if success:
    print("\nTrade result: Success! This trade receives more value than it gives away.\n")
    # Print additional metrics/reasoning here
else:
    print("\nTrade result: Don't do it! This trade gives away more value than it receives.\n")
    # Print additional metrics/reasoning here
