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

# Automate on Windows
create runnable jar and run it with jawis https://github.com/dfriedenberger/jawis

# Contact
Dirk Friedenberger, Waldaschaff, Germany

Write me (oder Schreibe mir)
projekte@frittenburger.de

http://www.frittenburger.de 

