# automate-your-life
Lightweight Open Source Enterprise Service Bus

# Build your Recipes (Use cases)
## Copy downloaded file to archive folder
```
		// Create a basic service object 
		Service service = new Service();

		// Create a directory resource and add to the service.
		Directory downloads = new Directory("C:\\Users\\frankenstein\\Downloads");
		service.addResource(downloads);
		
		// Create a recipe which moves bank documents to archive 
		// when is downloaded  
		service.addRecipe("Archive bank documents")
			.When(NewFile().in(downloads).withPattern("DeutscheBank*.pdf"))
			.Do(SaveFile().to("C:/archive/deutschebank"));
		
		// Start things up! 
		service.start();
		service.join();
```

## Extract bills from mail
```
		// Create a basic service object 
		Service service = new Service();

		// Create a mailbox resource and add to the service.
		MailBox mailBox = new MailBox("pop3","pop.gmx.net");
		mailBox.setCredentials("username","password");
		mailBox.setCylce(10);
		service.addResource(mailBox);
	
		// Create a recipe which extracts bills received by mail
		service.addRecipe("Archive bill from mail")
			.When(NewMail().in(mailBox).from("rechnungonline@telekom.de"))
			.Do(ExtractAttachment().withName("*.pdf"))
			.Do(SaveFile().to("C:/archive/telekom"));
		
		// Start things up! 
		service.start();
		service.join();
```

## Mapping full OpenWeatherMap json response to simple json extract
```
        // Create a basic service object 
		Service service = new Service();

		// Create a api resource and add to the service.
		WebApi openweather = new WebApi("https://api.openweathermap.org/data/2.5/weather"); 
		openweather.set("appid","- get the free api key from openweathermap.org -");
		openweather.set("q","Waldaschaff,DE");
		openweather.set("units","metric");
		openweather.setCylce(10);

		service.addResource(openweather);
		
		// Create a recipe which mapps the json data
		service.addRecipe("Receive Weatherdata")
				.When(NewResponse().in(openweather))
				.Do(Map().mapping(new Mapping<JsonNode, JsonNode>() {

					@Override
					public JsonNode map(JsonNode source) {

						ObjectMapper objectMapper = new ObjectMapper();
						ObjectNode target = objectMapper.createObjectNode();
						
						String name = source.path("name").textValue();
						target.put("city", name);

						Date date = new Date(source.path("dt").longValue() * 1000);
						SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH);
						target.put("date", sdf.format(date));
						
						float tempC = source.path("main").path("temp").floatValue();
						target.put("temp", tempC+" Celsius");
					
						return target;
					}
				}))
				.Do(SaveFile().to("C:/Temp/automate-your-life/arch"));
	
		// Start things up! 
		service.start();
		service.join();


```
#### original json from openweathermap.org
```
{
  "coord": {
    "lon": 9.3,
    "lat": 49.98
  },
  "weather": [
    {
      "id": 803,
      "main": "Clouds",
      "description": "broken clouds",
      "icon": "04n"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 4,
    "pressure": 1022,
    "humidity": 80,
    "temp_min": 4,
    "temp_max": 4
  },
  "visibility": 10000,
  "wind": {
    "speed": 7.7,
    "deg": 30
  },
  "clouds": {
    "all": 75
  },
  "dt": 1515342000,
  "sys": {
    "type": 1,
    "id": 4881,
    "message": 0.0076,
    "country": "DE",
    "sunrise": 1515309580,
    "sunset": 1515339559
  },
  "id": 2815180,
  "name": "Waldaschaff",
  "cod": 200
}
```
#### extracted json save to file
```
{
  "city": "Waldaschaff",
  "date": "Sunday 07 Jan 2018",
  "temp": "4.0 Celsius"
}
```

# Automate on Windows
create runnable jar and run it with jawis https://github.com/dfriedenberger/jawis

# Contact
Dirk Friedenberger, Waldaschaff, Germany

Write me (oder Schreibe mir)
projekte@frittenburger.de

http://www.frittenburger.de 

