# PCBE-Proiect2
Echipa este formată din:
	Nicolae Cristina,
	Gealapu Andreea,
	Culda Paula,
	Covaci Crina.
	
	
	Proiectul nostru implementează un sistem de știri. 
	Evenimentele care pot să apară în cadrul aplicației sunt evenimente de tipurile: Added(adăugarea unei știri), Deleted(ștergerea unei știri), Modified(modificarea unei știri), Read(citirea unei știri).
	Abonarea pentru aceste tipuri de evenimente se face și în funcție de domeniul de interes.
	Un Subscriber se poate abona la evenimente de tipul Added, Deleted și Modified, pe când un Publisher se poate abona la evenimentul de tipul Read prin care subscriberul notifică publisherul că el a citit știrea. 


Clasele și rolul lor:
	
	1. Actors
				= clasa abstractă care definește cele 2 tipuri de actori: Publisher/Editor și Subscriber/Cititor
				- un constructor în care sunt setate atributele name și service
				- un getter pentru atributul name
				- metoda abstractă callback

	2. Events
				= clasa în care sunt definite evenimentele
				- un enumerator în care avem evenimentele Added, Modified, Deleted, Read
				- un constructor în care sunt setate tipul evenimentului și mesajul
				- un al doilea constructor(overload) folosit atunci cand evenimentul este de tip Modified, deoarece avem nevoie să preluăm și noul text al știrii 
				- gettere si settere pentru fiecare atribut

	3. Message		
				= clasa în care se definește știrea
				- un constructor în care sunt setate domeniul, subdomeniul, autorul, sursa, textul știrii, data, dar și data modificării pentru fiecare știre
				- o metodă (un setter) - modifyText, pentru modificarea textului știrii. Aceasta este apelată când evenimentul este de tip Modified
				- gettere si settere pentru fiecare atribut
	
	
	4. SubscriptionOfTopic
				= clasa în care se definește un subscriber pentru un anumit domeniu
				- un constructor în care sunt setate domeniul și actorul
				- un getter pentru atributul actor
						
	
	5. PubSubService
				= clasa noastră dispatcher care primește mesaje de la publisheri și le va trimite subscriberilor (gestionează comunicarea dintre Publisher și Subscriber)
				- o coadă în care sunt puse evenimentele și un hashMap cu un String și o listă în care sunt puși subscriberii care se subscriu la evenimente
				- un constructor în care sunt puși in hashMap toți subscriberii pentru fiecare eveniment în parte
				- metoda addSubscriber prin care sunt adăugați subscriberii
				- metoda sendEvent prin care sunt trimise evenimentele, adică sunt adăugate în coada de evenimente
				- metoda broadcast prin care se ia fiecare eveniment din coadă (prin ștergerea lui) și se distribuie către abonații corespunzători 
					 Astfel, pentru fiecare subscriber, se apelează metoda callback, cea care conține business logic-ul ce se vrea implementat în momentul primirii unui eveniment de tipul respectiv. 
				- metoda run în care este apelată metoda broadcast (pentru thread)


	
	6. PublisherImpl
				= o clasă care extinde clasa abstractă Actors 
				- un hashMap, prin care pentru fiecare domeniu de știri se asociază un counter în care este reținut numărul cititorilor pentru fiecare domeniu în parte
					Astfel, de fiecare dată când apare un eveniment de tip Read, counter-ul se setează pe 1     (dacă a fost null, adică dacă încă nu citise nimeni știrea) sau se incrementează
				- un constructor în care sunt setate atributele name și service
				- metoda registerListener în care se apelează addSubscriber, metodă folosită pentru a adăuga subscriberii. Aceștia se abonează la un anumit tip de eveniment pentru știri dintr-un domeniu dorit 
				- metoda callBack care conține business logic-ul ce se vrea implementat în momentul primirii unui eveniment de tipul precizat
				- metoda run sunt apelate metodele specifice clasei
				
				
	7.  SubscriberImpl
				= o clasă care extinde clasa abstractă Actors 
				- o listă în care sunt reținute mesajele subscriberilor
				- un constructor în care sunt setate atributele name și service
				- metoda registerListener în care se apelează addSubscriber, metodă folosită pentru a adăuga subscriberii. Aceștia se abonează la un anumit tip de eveniment pentru știri dintr-un domeniu dorit 
				- metoda callBack care conține business logic-ul ce se vrea implementat în momentul primirii unui eveniment de tipul precizat
				- metoda run sunt apelate metodele specifice clasei
				
	8. Client_Main
				- am declarat publisherii, subscriberii și service-ul
				- am creat thread-urile pentru publisher, subscriber și service, iar apoi le-am pornit
