

def nameProbabilityMatch(google_name, imdb_name):
    checkLength = min(len(google_name), len(imdb_name))
    probabiltyCounter = 0
    for x in range(0, checkLength):
        if(google_name[x] == imdb_name[x]):
            probabiltyCounter += 1

    print(str(probabiltyCounter) + ' / ' + str(checkLength))
    print(float(probabiltyCounter)/float(checkLength))*100
    print(float(probabiltyCounter / checkLength))
    return (float(probabiltyCounter / checkLength) * 100)


print ("Name match: " + str(nameProbabilityMatch("Gello", "Goodbye")) + '%')