#Extensions to the Packing Application

## 1) Adding new Element and Sources
The first possible extension to this application would be to add 
new elements and sources. The way the application is implemented, for a new 
element to be extracted a new implementation of the AbstractExtractor class
should be implemented and a new Element value should be added to the
Extractor.Element enum. For a new source, a new Source value should be added
to the Extractor.Source enum, and a new abstract method should be added in 
the AbstractExtractor class. 
This could probably be handled better if a class was created for every
(Source X Element) pair.

## 2) Adding new products.
In the way the application works now, the extractor side could be wrapped as
a bookExtractor package and use its structure for other products.

## 3) Parsing and packing large amounts of books
A possible improvement to handle a larger quantity would be to directly
extract the books' information into Spark. In addition create a mapping
function which separates the books into boxes, where the box does not
have to be an explicit data structure but rather just a dataframe. 
This way the mapping could be performed in parallel.

## 4) Intelligent Extraction
There are several ways of how this could be done. One of the best ways would
be to create a pipeline where the first step is to extract the html element(s)
where the relevant information is stored. For this a model could be trained
taking the source code as input and as output the html element. This specific
step does not necessarily have to be done by a learning algorithm but it could 
be useful for generalization. 
The second step would be to use a Named Entity Recognition algorithm to extract
the wanted fields. For this learning model it would be wise to only feed the text
from the html element.

## 5) Data storage
A better way to store the data would be in a database. However, because for this task
we want to store data from the web it might be useful to not store it in a standard relational 
database rather in a graph database. This way when querying to create large meaningful datasets
we won't have to create expensive join statements, but rather use the power of a graph
data structure to transverse through related data.

