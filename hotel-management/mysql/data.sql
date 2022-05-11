use hotel_management;

-- Insert rows into table 'user'
insert into hotel_management.user (username, password, role, full_name, gender, year_of_birth)
values
('opallatina0', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 0, 'Obie Pallatina', '1', 1988),
('mcommusso1', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 1, 'Mikael Commusso', '1', 1985),
('lgurney2', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 0, 'Lovell Gurney', '1', 1987),
('mlackie3', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 1, 'Meyer Lackie', '1', 1997),2
('fdenacamp4', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 1, 'Ford Denacamp', '1', 1997),
('aschimpke5', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 1, 'Averell Schimpke', '1', 1980),
('jvinson6', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 0, 'Jemmy Vinson', '0', 1984),
('vmcgeffen7', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 0, 'Veradis McGeffen', '0', 1980),
('aramsay8', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 1, 'Ashley Ramsay', '0', 1991),
('smorales9', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', 1, 'Slade Morales', '1', 1988),
('ranscott0', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Rayner Anscott', '1', 1982),
('eeadmeads1', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Eleonore Eadmeads', '0', 1993),
('llibermore2', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Ludvig Libermore', '1', 1986),
('jlongthorne3', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Jeremie Longthorne', '1', 1993),
('sjerromes4', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Shayla Jerromes', '0', 1986),
('gfidal5', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Geoffrey Fidal', '1', 1994),
('vwallerbridge6', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 0, 'Vere Waller-Bridge', '0', 1993),
('adunkerton7', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Annemarie Dunkerton', '0', 1996),
('jtilzey8', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 0, 'Joann Tilzey', '0', 1996),
('cduferie9', '439087482d21d57584b6d1a26cc9e3844ed75c36f33c0f7952042ab0412b2405', 1, 'Chris Duferie', '0', 1999);

insert into hotel_management.room_type (name, price)
values
('Affordable', 70),
('Normal', 100),
('Luxury', 200);

insert into hotel_management.room (name, description, status, room_type_id)
values
('Room 101', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 102', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 0, 1),
('Room 103', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 104', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 105', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 0, 1),
('Room 106', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 107', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 108', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 109', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 0, 1),
('Room 110', 'As our smallest budget rooms, the Affordable bedrooms are suited for single occupancy or short-stay double occupancy as they have limited space and storage.', 1, 1),
('Room 211', 'Normal room has a seating area, ample storage, digital safe and mini fridge. This room can also be configured with an extra roll-away bed for families of 3.', 1, 2),
('Room 212', 'Normal room has a seating area, ample storage, digital safe and mini fridge. This room can also be configured with an extra roll-away bed for families of 3.', 1, 2),
('Room 213', 'Normal room has a seating area, ample storage, digital safe and mini fridge. This room can also be configured with an extra roll-away bed for families of 3.', 1, 2),
('Room 214', 'Normal room has a seating area, ample storage, digital safe and mini fridge. This room can also be configured with an extra roll-away bed for families of 3.', 0, 2),
('Room 215', 'Normal room has a seating area, ample storage, digital safe and mini fridge. This room can also be configured with an extra roll-away bed for families of 3.', 1, 2),
('Room 316', 'Our Luxury room provides views over landscaped gardens. It has a seating area, ample storage, digital safe and mini fridge.', 1, 3),
('Room 317', 'Our Luxury room provides views over landscaped gardens. It has a seating area, ample storage, digital safe and mini fridge.', 1, 3),
('Room 318', 'Our Luxury room provides views over landscaped gardens. It has a seating area, ample storage, digital safe and mini fridge.', 1, 3),
('Room 319', 'Our Luxury room provides views over landscaped gardens. It has a seating area, ample storage, digital safe and mini fridge.', 1, 3),
('Room 320', 'Our Luxury room provides views over landscaped gardens. It has a seating area, ample storage, digital safe and mini fridge.', 0, 3);

insert into hotel_management.rental_invoice (start_date, end_date, room_id, room_name, room_type_id, room_type_name, room_type_price, customer_name, identity_number, address, customer_type, is_paid)
VALUES
('2022-03-01 00:00:00', '2022-03-02 23:59:59', 1, 'Room 101', 1, 'Affordable', 70, 'John Doe', '456783472', '5100 Oakland Dr Rio, Wisconsin(WI)', 0, 1),
('2022-03-03 00:00:00', '2022-03-04 23:59:59', 3, 'Room 103', 1, 'Affordable', 70, 'Anna Kim', '912456165', '405 1st Ave Brookings, South Dakota(SD)', 0, 0),
('2022-03-04 00:00:00', '2022-03-05 23:59:59', 4, 'Room 104', 1, 'Affordable', 70, 'Kenny John', '234578952', '13824 Baker Rd Durand, Illinois(IL)', 0, 1),
('2022-03-06 00:00:00', '2022-03-07 23:59:59', 6, 'Room 106', 1, 'Affordable', 70, 'Sarah Betty', '278156712', '123 Russell St Everett, Massachusetts(MA)', 0, 0),
('2022-03-07 00:00:00', '2022-03-08 23:59:59', 7, 'Room 107', 1, 'Affordable', 70, 'Anthony Simon', '123678496', '190 Woodcroft Dr Youngsville, North Carolina(NC)', 0, 1),
('2022-03-08 00:00:00', '2022-03-09 23:59:59', 8, 'Room 108', 1, 'Affordable', 70, 'Andy Higg', '257931561', '639 Lemke Dr Placentia, California(CA)', 0, 0),
('2022-03-10 00:00:00', '2022-03-11 23:59:59', 10, 'Room 110', 1, 'Affordable', 70, 'Bob Dunson', '197283455', '144 County 81 Rd Woodland, Mississippi(MS)', 0, 1),
('2022-03-11 00:00:00', '2022-03-13 23:59:59', 11, 'Room 211', 2, 'Normal', 100, 'Cindy Hugh', '891245763', '9311 Dorrington Pl Pacoima, California(CA)', 0, 0),
('2022-03-12 00:00:00', '2022-03-14 23:59:59', 12, 'Room 212', 2, 'Normal', 100, 'Clara Bill', '912678576', '3885 Housers Mill Rd Byron, Georgia(GA)', 0, 0),
('2022-03-13 00:00:00', '2022-03-15 23:59:59', 13, 'Room 213', 2, 'Normal', 100, 'Nancy Drew', '125789346', '6160 W 8th St Tulsa, Oklahoma(OK)', 0, 1),
('2022-03-15 00:00:00', '2022-03-18 23:59:59', 15, 'Room 315', 3, 'Luxury', 200, 'Gibson Hope', '918267342', '146 Park Ave Arlington, Massachusetts(MA)', 1, 1),
('2022-03-16 00:00:00', '2022-03-19 23:59:59', 16, 'Room 316', 3, 'Luxury', 200, 'Kim Ash', '790234565', '207 Live Oak Ave Belmont, North Carolina(NC)', 1, 0),
('2022-03-17 00:00:00', '2022-03-20 23:59:59', 17, 'Room 317', 3, 'Luxury', 200, 'Lucas Hopper', '789234512', '1901 Winston Dr Saint Marys, Ohio(OH)', 1, 0),
('2022-03-18 00:00:00', '2022-03-21 23:59:59', 18, 'Room 318', 3, 'Luxury', 200, 'Maria Acharteur', '256789423', '33 Highland Dr Caldwell, Ohio(OH)', 1, 0),
('2022-03-19 00:00:00', '2022-03-22 23:59:59', 19, 'Room 319', 3, 'Luxury', 200, 'Thomas Anderson', '578234612', '1403 S Robin Way Rushville, Indiana(IN)', 1, 0);

insert into hotel_management.rental_receipt (start_date, end_date, price, total_price, room_name, room_type_name, room_id)
values
('2022-03-01 00:00:00', '2022-03-02 23:59:59', 70, 70, 'Room 101', 'Affordable', 1),
('2022-03-03 00:00:00', '2022-03-04 23:59:59', 70, 70, 'Room 103', 'Affordable', 3),
('2022-03-04 00:00:00', '2022-03-05 23:59:59', 70, 70, 'Room 104', 'Affordable', 4),
('2022-03-06 00:00:00', '2022-03-07 23:59:59', 70, 70, 'Room 106', 'Affordable', 6),
('2022-03-07 00:00:00', '2022-03-08 23:59:59', 70, 70, 'Room 107', 'Affordable', 7),
('2022-03-08 00:00:00', '2022-03-09 23:59:59', 70, 70, 'Room 108', 'Affordable', 8),
('2022-03-10 00:00:00', '2022-03-11 23:59:59', 70, 70, 'Room 110', 'Affordable', 10),
('2022-03-11 00:00:00', '2022-03-13 23:59:59', 100, 200, 'Room 211', 'Normal', 11),
('2022-03-12 00:00:00', '2022-03-14 23:59:59', 100, 200, 'Room 212', 'Normal', 12),
('2022-03-13 00:00:00', '2022-03-15 23:59:59', 100, 200, 'Room 213', 'Normal', 13),
('2022-03-15 00:00:00', '2022-03-18 23:59:59', 200, 600, 'Room 315', 'Luxury', 15),
('2022-03-16 00:00:00', '2022-03-19 23:59:59', 200, 600, 'Room 316', 'Luxury', 16),
('2022-03-17 00:00:00', '2022-03-20 23:59:59', 200, 600, 'Room 317', 'Luxury', 17),
('2022-03-18 00:00:00', '2022-03-21 23:59:59', 200, 600, 'Room 318', 'Luxury', 18),
('2022-03-19 00:00:00', '2022-03-22 23:59:59', 200, 600, 'Room 319', 'Luxury', 19);

insert into service(name, description, price, notes)
values
('Buffet', 'Join our buffet and be delighted in a wide selection of delicious delicacies, premium quality seafood includes rock lobsters, fresh oysters from France, delectable mud crabs and blue crabs, fresh prawns, mouth-watering salmon, a wide assortment of shellfish, authentic sushi & sashimi and sweet treats at our buffet.', 20, 'Free for children below 1.2m. Can not use over 2 hours.'),
('Bar', 'Enjoy a drink in our spacious lounge bar, is a truly enjoyable experience – the views of the natural harbour, no ships just a variety of birds and may be even spot a stray seal or penguin and then across the water with the opposite side comprising of natural terrain, with the names of ships picked out by natural stones will help to ensure that you will have a “relaxing drink”', 10, 'Bar is only open from 3:00 PM to 00:00 AM'),
('Golf', 'Our golf course is set in parkland with extensive woodland areas.  Beech and oak trees, characteristic of this part of Buckinghamshire, feature on many holes and as you can imagine the course is particularly attractive in Spring and Autumn.', 60, 'You need to bring your own golf equipment'),
('Car Rental', 'Our car rental service is available 24/7. We provide a wide range of car rental services including private transfer, shared transfer, and car transfer. We also provide car rental services for our guests who are staying in our hotel.', 80, 'We have a large fleet of cars available for rent.');


insert into hotel_management.service_invoice(service_name, number_of_customers, price, notes, room_id, room_name, service_id)
values
('Buffet', 2, 20, 'Include 1 children (below 1.2m)', 17, 'Room 317', 1),
('Bar', 1, 10, 'Broke 1 wine glass, charge for $10' ,7, 'Room 107', 2),
('Golf', 3, 60, 'Customer lost a hat during play', 19, 'Room 319', 3),
('Car Rental', 2, 80, 'Need a 7-seat car', 20, 'Room 320', 4);


insert into `hotel_management`.product(name, price, stock, description, product_type)
values
('Onion Soup', 12, 35, 'Onion soup is a type of vegetable soup with sliced onions as the main ingredient. It is prepared in different variations in many different countries, the most famous of which is the French onion soup or Parisian onion soup. Because of the affordable ingredients, it has primarily been a dish for the poor for a long time.', 0),
('Ceasar Salad', 15, 42, 'A Caesar salad (also spelled Cesar and Cesare) is a green salad of romaine lettuce and croutons dressed with lemon juice (or lime juice), olive oil, egg, Worcestershire sauce, anchovies, garlic, Dijon mustard, Parmesan cheese, and black pepper.', 0),
('Spring Rolls', 10, 63, 'Spring rolls are rolled appetizers or dim sum commonly found in Chinese and other Southeast Asian cuisines. The kind of wrapper, fillings, and cooking technique used, as well as the name, vary considerably within this large area, depending on the region\'s culture.', 0),
('Crab Bisque Soup', 12, 54, 'A decadent bisque made with real crabmeat and thickened with cream and topped off with a bit of Sherry! A bisque is a French style of soup that is made from lobster, crab or shrimp.', 0),
('Grilled Tuna with Lemon Sauce', 18, 50, 'Flaky, firm, and with the perfect amount of fish flavor, tuna steaks are a nutritious meal. Easy to make, the steaks are seasoned with fresh and simple ingredients. The result is a chunky and juicy fish steak covered in a tangy and creamy lemon sauce.', 0),
('Steak Diane', 40, 60, 'Steak Diane is a dish of pan-fried beefsteak with a sauce made from the seasoned pan juices, generally prepared in restaurants tableside, and sometimes flambéed. It was probably invented in London or New York in the 1930s. From the 1940s through the 1960s, it was a standard dish in "Continental cuisine", and is now considered retro.', 0),
('Escalope of Chicken with Boiled Potato', 16, 57, 'You’ll make an easy spin on the classic Central European dish by coating chicken thighs in flour, egg, and breadcrumbs before searing them in the pan to achieve the characteristically crunchy exterior—perfectly contrasted with a spoonful of tangy-sweet marinated apple. A side of kale dressed in a quick honey mustard sauce cuts through the richness of the dish.', 0),
('Caramel Custard', 6, 70, 'Crème caramel, flan, caramel pudding or caramel custard is a custard dessert with a layer of clear caramel sauce.', 0),
('Banana Flambees', 14, 40, 'Bananas Foster is a dessert made from bananas and vanilla ice cream, with a sauce made from butter, brown sugar, cinnamon, dark rum, and banana liqueur.[2] The butter, sugar and bananas are cooked, and then alcohol is added and ignited. The bananas and sauce are then served over the ice cream. Popular toppings also include whipped cream and different types of nuts (pecans, walnuts, etc.). The dish is often prepared tableside as a flambé.', 0),
('Chateaubriand', 42, 56, 'Chateaubriand (sometimes called chateaubriand steak) is a dish that traditionally consists of a large center cut fillet of tenderloin grilled between two lesser pieces of meat that are discarded after cooking. While the term originally referred to the preparation of the dish, Auguste Escoffier named the specific center cut of the tenderloin the Chateaubriand.', 0),
('Coke', 5, 110, 'Coca-Cola, or Coke, is a carbonated soft drink manufactured by the Coca-Cola Company. Originally marketed as a temperance drink and intended as a patent medicine, it was invented in the late 19th century by John Stith Pemberton in Atlanta, Georgia.', 1),
('Pepsi', 5, 120, 'Pepsi is a carbonated soft drink manufactured by PepsiCo. Originally created and developed in 1893 by Caleb Bradham and introduced as Brad\'s Drink, it was renamed as Pepsi-Cola in 1898, and then shortened to Pepsi in 1961.', 1),
('Sapporo', 10, 60, 'Sapporo Breweries Ltd. is a Japanese beer brewing company founded in 1876. Sapporo is the oldest brand of beer in Japan. It was first brewed in Sapporo, Japan, in 1876 by brewer Seibei Nakagawa. The world headquarters of Sapporo Breweries is in Ebisu, Shibuya, Tokyo. The company purchased the Canadian company Sleeman Breweries in 2006.', 1),
('Red Wine', 400, 40, 'Red wine is a type of wine made from dark-colored grape varieties. The color of the wine can range from intense violet, typical of young wines, through to brick red for mature wines and brown for older red wines. The juice from most purple grapes is greenish-white, the red color coming from anthocyan pigments present in the skin of the grape. Much of the red wine production process involves extraction of color and flavor components from the grape skin.', 1),
('White Wine', 350, 29, 'White wine is a wine that is fermented without skin contact. The colour can be straw-yellow, yellow-green, or yellow-gold. It is produced by the alcoholic fermentation of the non-coloured pulp of grapes, which may have a skin of any colour. White wine has existed for at least 4,000 years.', 1),
('Champange', 280, 33, 'Champagne is a sparkling wine originated and produced in the Champagne wine region of France under the rules of the appellation, that demand specific vineyard practices, sourcing of grapes exclusively from designated places within it, specific grape-pressing methods and secondary fermentation of the wine in the bottle to cause carbonation.', 1),
('Cocktail', 70, 34, 'A cocktail is an alcoholic mixed drink. Most commonly, cocktails are either a combination of spirits, or one or more spirits mixed with other ingredients such as tonic water, fruit juice, flavored syrup, or cream. Cocktails vary widely across regions of the world, and many websites publish both original recipes and their own interpretations of older and more famous cocktails.', 1),
('Mineral Water', 4, 80, 'Mineral water is water from a mineral spring that contains various minerals, such as salts and sulfur compounds. Mineral water may usually be still or sparkling (carbonated/effervescent) according to the presence or absence of added gases.', 1),
('Sparkling Water', 8, 19, 'Carbonated water (also known as soda water, sparkling water, fizzy water, club soda, water with gas, in many places as mineral water or (especially in the U.S.) as seltzer or seltzer water) is water containing dissolved carbon dioxide gas, either artificially injected under pressure or occurring due to natural geological processes. Carbonation causes small bubbles to form, giving the water an effervescent quality. Common forms include sparkling natural mineral water, club soda, and commercially-produced sparkling water.', 1),
('Vodka', 140, 20, 'Vodka is a clear distilled alcoholic beverage. Different varieties originated in Poland, Russia, and Sweden. Vodka is composed mainly of water and ethanol but sometimes with traces of impurities and flavourings. Traditionally, it is made by distilling liquid from fermented cereal grains. Potatoes have been used in more recent times, and some modern brands use fruits, honey, or maple sap as the base.', 1),
('Umbrella', 13, 50, 'An umbrella or parasol is a folding canopy supported by wooden or metal ribs that is usually mounted on a wooden, metal, or plastic pole. It is designed to protect a person against rain or sunlight. The term umbrella is traditionally used when protecting oneself from rain, with parasol used when protecting oneself from sunlight, though the terms continue to be used interchangeably.', 2),
('Cap', 8, 22, 'A cap is a flat headgear, usually with a visor. Caps have crowns that fit very close to the head. They made their first appearance as early as 3,200BC. Caps typically have a visor, or no brim at all. They are popular in casual and informal settings, and are seen in sports and fashion. They are typically designed for warmth, and often incorporate a visor to block sunlight from the eyes. They come in many shapes, sizes, and are of different brands. Baseball caps are one of the most common types of cap.', 2),
('Fountain Pen', 30, 30, 'A fountain pen is a writing instrument which uses a metal nib to apply a water-based ink to paper. It is distinguished from earlier dip pens by using an internal reservoir to hold ink, eliminating the need to repeatedly dip the pen in an inkwell during use. The pen draws ink from the reservoir through a feed to the nib and deposits the ink on paper via a combination of gravity and capillary action. Filling the reservoir with ink may be achieved manually, via the use of an eyedropper or syringe, or via an internal filling mechanism which creates suction (for example, through a piston mechanism) or a vacuum to transfer ink directly through the nib into the reservoir. Some pens employ removable reservoirs in the form of pre-filled ink cartridges.', 2),
('Shirt', 25, 40, 'A shirt is a cloth garment for the upper body (from the neck to the waist).', 2),
('Coffee Cup', 17, 25, 'A coffee cup is a container that coffee and espresso-based drinks are served in. Coffee cups are typically made of glazed ceramic, and have a single handle for portability while the beverage is hot. Ceramic construction allows a beverage to be drunk while hot, providing insulation to the beverage, and quickly washed with cold water without fear of breakage, compared to typical glassware.', 2),
('Keychain', 3, 26, 'A keychain (also key fob or keyring) is a small ring or chain of metal to which several keys can be attached. The length of a keychain allows an item to be used more easily than if connected directly to a keyring. Some keychains allow one or both ends the ability to rotate, keeping the keychain from becoming twisted, while the item is being used.', 2),
('Jewelry', 1700, 20, 'Jewellery or jewelry consists of decorative items worn for personal adornment, such as brooches, rings, necklaces, earrings, pendants, bracelets, and cufflinks. Jewellery may be attached to the body or the clothes. From a western perspective, the term is restricted to durable ornaments, excluding flowers for example. For many centuries metal such as gold often combined with gemstones, has been the normal material for jewellery, but other materials such as shells and other plant materials may be used.', 2);

insert into hotel_management.receipt(customer_name, purchased_date, notes, total_price)
values
('Anna Kim', '2022-03-04 00:00:00', 'None', 51),
('Kenny John', '2022-03-05 00:00:00', 'Discount 10%', 75),
('Anthony Simon', '2022-03-08 00:00:00', 'None', 100),
('Andy Higg', '2022-03-09 00:00:00', 'Pay additional 5$ for service fee', 40),
('Cindy Hugh', '2022-03-13 00:00:00', 'None', 77),
('Clara Bill', '2022-03-14 00:00:00', 'None', 89),
('Nancy Drew', '2022-03-15 00:00:00', 'Discount 15%', 2890),
('Gibson Hope', '2022-03-18 00:00:00', 'Discount 5%', 311),
('Kim Ash', '2022-03-19 00:00:00', 'None', 148),
('Lucas Hopper', '2022-03-20 00:00:00', 'Not include VAT', 718);

insert into hotel_management.receipt_detail(receipt_id, quantity, product_id, product_name, product_type, price)
values
(1, 2, 18, 'Mineral Water', 1, 4),
(1, 1, 6, 'Steak Diane', 0, 40),
(1, 1, 26, 'Keychain', 2, 3),
(2, 1, 4, 'Crab Bisque Soup', 0, 12),
(2, 1, 7, 'Escalope of Chicken with Boiled Potato', 0, 16),
(2, 3, 13, 'Sapporo', 1, 10),
(2, 1, 24,'Shirt', 2, 25),
(3, 1, 17, 'Cocktail', 1, 70),
(3, 1, 1, 'Onion Soup', 0, 12),
(3, 1, 5, 'Grilled Tuna with Lemon Sauce', 0, 18),
(4, 1, 21, 'Umbrella', 2, 13),
(4, 2, 8, 'Caramel Custard', 0, 6),
(4, 2, 11, 'Coke', 1, 5),
(5, 1, 25, 'Coffee Cup', 2, 17),
(5, 2, 23, 'Fountain Pen', 2, 30),
(6, 1, 19, 'Sparkling Water', 1, 8),
(6, 1, 2, 'Ceasar Salad', 0, 15),
(6, 1, null, 'Lobster Thermidor', 0, 52),
(6, 1, 9, 'Banana Flambees', 0, 14),
(7, 2, 27, 'Jewelry', 2, 1700),
(8, 1, 10, 'Chateaubriand', 0, 42),
(8, 1, 16, 'Champange', 1, 280),
(8, 1, 12, 'Pepsi', 1, 5),
(9, 1, 20, 'Vodka', 1, 140),
(9, 1, 22, 'Cap', 2, 8),
(10, 2, 15, 'White Wine', 1, 350),
(10, 1, 4, 'Crab Bisque Soup', 0, 12),
(10, 1, 8, 'Caramel Custard', 0, 6);

insert into hotel_management.import_invoice(imported_date, notes, total_price)
values
('2022-02-11 00:00:00', 'None', 1445),
('2022-02-12 00:00:00', 'Bank transfer', 3660),
('2022-02-13 00:00:00', 'None', 9547),
('2022-02-14 00:00:00', 'Bank transfer', 7453);

insert into hotel_management.import_invoice_detail(import_invoice_id, quantity, product_id, product_name, product_type, price)
values
(1, 30, 4, 'Crab Bisque Soup', 0, 12),
(1, 20, 9, 'Banana Flambees', 0, 14),
(1, 45, 12, 'Pepsi', 1, 5),
(1, 10, 21, 'Umbrella', 2, 13),
(1, 15, 23, 'Fountain Pen', 2, 30),
(2, 20, null, 'Lobster Thermidor', 0, 52),
(2, 40, 6, 'Steak Diane', 0, 40),
(2, 32, 13, 'Sapporo', 1, 10),
(2, 10, 17, 'Cocktail', 1, 70),
(3, 5,  27, 'Jewelry', 2, 1700),
(3, 35, 24, 'Shirt', 2, 25),
(3, 14, 22, 'Cap', 2, 8),
(3, 20, 26, 'Keychain', 2, 3),
(4, 42, 3, 'Spring Rolls', 0, 10),
(4, 20, 5, 'Grilled Tuna with Lemon Sauce', 0, 18),
(4, 16, 10, 'Chateaubriand', 0, 42),
(4, 15, 14, 'Red Wine', 1, 400);
