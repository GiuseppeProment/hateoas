##How to compile and run the application with an example for each call.

**mvn spring-boot:run** 

The application is HATEOAS (Hypermedia as the Engine of Application State) compliant.

urls:

###Create product
curl -i -X POST -H "Content-Type:application/json" -d "{  \"name\" : \"banana\",  \"description\" : \"fruta\" }" http://localhost:8080/products

*result*

HTTP/1.1 201
 
Location: http://localhost:8080/products/4

...
###Update product
curl -i -X PATCH -H "Content-Type:application/json" -d "{  \"description\" : \"fruta tropical\" }" http://localhost:8080/products/4

*result*

HTTP/1.1 200
 
...
###Delete product
curl -i -X DELETE -H "Content-Type:application/json" http://localhost:8080/products/4

*result*

HTTP/1.1 204
 
...

###Create image
curl -i -X POST -H "Content-Type:application/json" -d "{  \"type\" : \"extra large\" }" http://localhost:8080/images

*result*

HTTP/1.1 201
 
Location: http://localhost:8080/images/7

...

###Update image
curl -i -X PATCH -H "Content-Type:application/json" -d "{  \"type\" : \"medium\" }" http://localhost:8080/images/7

*result*

HTTP/1.1 200 

...

###Delete image
curl -i -X DELETE -H "Content-Type:application/json" http://localhost:8080/images/7

*result*

HTTP/1.1 204 

...

###Remarks
the database is initialized on startup with data (refer to Fixture item ahead)

###Get all products excluding relationships (child products, images)
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products/

###Get all products including child product and images
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products?projection=full
 
###Get all products including child product
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products?projection=children

###Get all products including images 
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products?projection=images

###Get specific product including child product and images
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products/1?projection=full
 
###Get specific product including child product
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products/1?projection=children

###Get  specific product including images 
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products/1?projection=images

###Get  specific product excluding relationships (child products, images)
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products/1

###Get set of child products for specific product 
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080/products/1/products

###Get set of images for specific product
curl -i -X GET -H "Content-Type:application/json" http://localhost:8080//products/1/images

##How to run the suite of automated tests.
 
**mvn test**

##Fixture (data present on application initialization)
{
  "_embedded" : {
    "products" : [ {
      "images" : [ {
        "type" : "medium"
      }, {
        "type" : "small"
      } ],
      "products" : [ {
        "name" : "selim",
        "description" : "basic selim"
      }, {
        "name" : "wheel",
        "description" : "basic wheel"
      } ],
      "description" : "basic bike",
      "name" : "bike",
      "id" : 1,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/products/1"
        },
        "product" : {
          "href" : "http://localhost:8080/products/1{?projection}",
          "templated" : true
        },
        "parent" : {
          "href" : "http://localhost:8080/products/1/parent"
        },
        "images" : {
          "href" : "http://localhost:8080/products/1/images"
        },
        "products" : {
          "href" : "http://localhost:8080/products/1/products"
        }
      }
    }, {
      "images" : [ {
        "type" : "medium"
      }, {
        "type" : "small"
      } ],
      "products" : [ ],
      "description" : "basic selim",
      "name" : "selim",
      "id" : 2,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/products/2"
        },
        "product" : {
          "href" : "http://localhost:8080/products/2{?projection}",
          "templated" : true
        },
        "parent" : {
          "href" : "http://localhost:8080/products/2/parent"
        },
        "images" : {
          "href" : "http://localhost:8080/products/2/images"
        },
        "products" : {
          "href" : "http://localhost:8080/products/2/products"
        }
      }
    }, {
      "images" : [ {
        "type" : "medium"
      }, {
        "type" : "small"
      } ],
      "products" : [ ],
      "description" : "basic wheel",
      "name" : "wheel",
      "id" : 3,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/products/3"
        },
        "product" : {
          "href" : "http://localhost:8080/products/3{?projection}",
          "templated" : true
        },
        "parent" : {
          "href" : "http://localhost:8080/products/3/parent"
        },
        "images" : {
          "href" : "http://localhost:8080/products/3/images"
        },
        "products" : {
          "href" : "http://localhost:8080/products/3/products"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/products{?page,size,sort,projection}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/products"
    },
    "search" : {
      "href" : "http://localhost:8080/products/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 3,
    "totalPages" : 1,
    "number" : 0
  }
}
