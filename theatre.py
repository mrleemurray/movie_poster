###cinema locator###

from typing import Any
import urllib.request
import json
import config as keys

class Theatre:
    def __init__(self, name):
        self.addressData: Any = self.retrieveAddressJSON(name)
        self.displayName = self.addressData["result"]["name"]
        self.zipcode = self.addressData["result"]["address_components"][len(self.addressData["result"]["address_components"])-1]["long_name"]
        self.region = self.addressData["result"]["address_components"][len(self.addressData["result"]["address_components"])-2]["short_name"]

    def retrieveAddressJSON(self, name):
        try:
            name = name.replace(' ', "%20").encode('ascii', 'ignore').decode('ascii')
            url = 'https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%(input)s&inputtype=textquery&fields=name,place_id&key=%(apiKey)s'
            searchResult = self.makeApiCall(url, name, keys.config['googlePlacesApiKey'])
            placeId = searchResult['candidates'][0]['place_id']
            url = 'https://maps.googleapis.com/maps/api/place/details/json?placeid=%(input)s&fields=name,address_component&key=%(apiKey)s'
            addressJSON = self.makeApiCall(url, placeId, keys.config['googlePlacesApiKey'])
            return addressJSON
        except Exception:
            return None

    def makeApiCall(self, url, input, apiKey):
        assembledUrl = url % {"apiKey" : apiKey, "input" : input}
        response = urllib.request.urlopen(assembledUrl)
        return json.loads(response.read())