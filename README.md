# FoodieHoodieV1 : Ingredient based recipe search engine

# Overview
FoodieHoodie is an idea to help people on basis of What You Have Is What You Cook. This project is built on Android framework, Java/J2EE, REST API and MySQL DB. We are building an Android Application with an easy to use UI. This project constitutes of web crawler, parser, indexer, query processor and awesome Android UI.<br>
Used <em><b>Early termination, top-20 shuffled result algorithm</b></em> and ranking is based on <em><b>relevance</b></em> of ingredient in recipe[Tough to rank the recipe, we need to think of more constraints].

# Web Crawler
•	Used seeds<br>
•	Seeds information acquired by personal experience, recommendation from friends & family<br>
•	Crawled pages and document id & page link saved in file<br>
•	Crawled 20,000+ recipes.<br>

# Parser
•	Used JSoup Parser, a brilliant DOM based parser where we can easily query it using jquery like syntax.<br>
•	Parser parses the recipe html file >> structure the html in DOM >> retrieve cook time, prep time, ingredients and method. [Right now we have left image to be parsed]<br>
•	Break the ingredients into individual unit and method into multiple steps.<br>
•	Parsed data is saved in database. Recipe is saved in recipe table and ingredients list is saved in process queue.<br>

# Indexer
•	Indexer pulls data from process queue and breaks each ingredient then checks if ingredient is present in the main table or not.<br>
•	If ingredient is present in main table then the indexer increases the weight by 1 and updates the docId and ingredients id in relationship table.<br>
•	If Ingredient is not present then it goes to new ingredient found table and a web interface is used to validate new ingredient in system. <br>
•	This web interface is very powerful and built to make Ingredient Dictionary.<br>
 
# Query Processor
•	Query Processor is powerful SQL query builder<br>
•	Ingredients are searched first in conjunction mode, where we have used basic math function like Union, Intersection and Difference of ingredient sets.<br>
•	User receives 20 result and if conjunctive search doesn’t provide 20 then query processor uses disjunctive search.<br>

# Data storage: 
We divided crawler, parser, indexer and android app as separate component and built it separately and integrated together afterwards. We used sql fantastically where we have normalized and indexed the sql tables.<br>

# How to use application
1.	User will put the ingredient and press enter and repeats till he enters all ingredient and press the top button.<br>
2.	 Ingredients go to REST API and JSON response populates in Recipe objects.<br>
3.	 With popular recipe coming on top, the entire eligible recipe would come as result.<br>
4.	 User can select and enjoy food.<br>

# Advanced Features to be used
1.	       Recipe Ranking mechanism ( based on location and user taste)<br>
2.	       Camera<br>
3.	       Social media connection<br>

# Functionalities
1. Prepopulated ingredient list in a gridview.
2. New Ingredient can be added dynamically by entering the text in textfield and pressing enter.
3. Repeat Step 2 till you enter all your ingredients. Starting screens look like below screenshots. You also have Help button which explains how to use the application. 
4. Once all ingredients are entered you can press > . 
5. This search calls the REST API on AWS cloud and convert the JSON object into Recipe Array object and displays all the list of recipe in List view. 
6. User selects a recipe and view it. As soon as he views the recipe the recipe saves in Recent table. 
7.User can also favorite the recipe. 
8.User can also see which website has the recipe and click on it can and check it on browser. Checkout screenshot 6,7,8
9.User can also check recent and favorite recipes from homescreen.
10. User can Add Recipe and that recipe gets saved in favorite list.
* Ingredients and Steps should be entered as Comma separated.




# Contributor
<b>Sourabh Taletiya (N15776267)</b><br>
st2654@nyu.edu<br>
201-680-8527<br>                                      

<b>Jay Patel ( N10541249 )</b><br>
jmp840@nyu.edu<br>
408-826-7731    <br>

# Professor
<b>Rumi Chunara</b><br>
