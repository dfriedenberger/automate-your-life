# automate-your-life
Lightweight Open Source Enterprise Service Bus

# Build your Recipes
## Copy downloaded file to archive folder
```
		// Create a basic service object 
		Service service = new Service();

		// Create a directory resource and add to the service.
		Directory downloads = new Directory("C:\\Users\\frankenstein\\Downloads");
		service.addResource(downloads);
		
		// Create a recipe which move bank documents to archive 
		// when is downloaded  
		service.addRecipe("Archive bank documents")
				.When(NewFile().in(downloads).withPattern("DeutscheBank*.pdf"))
				.Do(MoveFile().to("C:/archive/deutschebank"));
		
		// Start things up! By using the service.join() the server thread will join with the current thread.
                // See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
		service.start();
		service.join();
```

## Extract bills from mail
```
		// Create a basic service object 
		Service service = new Service();

		// Create a directory resource and add to the service.
		MailBox mailBox = new MailBox("pop3","pop.gmx.net");
		mailBox.setCredentials("username","password");
		mailBox.setCylce(10);
		service.addResource(mailBox);
	
		// Create a recipe which extracts bills received by mail
		service.addRecipe("Archive bill from mail")
			.When(NewMail().in(mailBox).from("rechnungonline@telekom.de"))
			.Do(ExtractAttachment().withName("*.pdf"))
			.Do(CopyFile().to("C:/archive/telekom"));
		
		// Start things up! By using the service.join() the server thread will join with the current thread.
                // See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
		service.start();
		service.join();
```

