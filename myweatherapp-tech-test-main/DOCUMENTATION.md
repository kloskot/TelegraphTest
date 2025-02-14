## HOW THE CODE WORKS

**Daylight Hours Comparison** I have implemented two functions for this task : getSunTimings() and findDifference().

getSunTimings(String city): extracts the data needed (sunrise and sunset) and calculates the time between them and returns a single figure

findDifference(String city1, String city2): is the endpoint function which uses the figures from getSunTimings() and compares the values returned for each of the city

**City it is raining in** I have implemented two functions for this task : isRaining() and rainStatus().

isRaining(String city): extracts the description of the city parsed and checks if the word 'rain' is mentioned (regardless of case) as this is the indicator for rain and returns the appropriate boolean

rainStatus(String city1, String city2): returns the appropriate response based on which city it is raining in and which one it isn't.

## WHY LIKE THIS

I have chosen to organise my code into two distinct functions for each task. One of the functions handles the operations (calculating, searching) and the other handles the comparison between the two outputs derived from the operations. This is the way I decomposed the problem. It allows for easy readability and reuse of code if need be.

## EXTRA

Throughout my code I have written comments explaining key points and other decisions I have made throughout development. I have also included notes on possible areas of improvement that I would explore given more time. 

I appreciate the time taken processing this and hope to hear back from you soon.