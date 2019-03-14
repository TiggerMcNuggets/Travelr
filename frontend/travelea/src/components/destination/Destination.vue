/* eslint-disable */

<template>
  <div class="outer-container">
    <div class="banner">
      <h1>MY DESTINATIONS</h1>
      <hr>
    </div>
           
    <div class="container">

      
    <a href="http://localhost:8080/#/destination/create" ><v-btn class='update-button'>CREATE DESTINATION</v-btn></a>

      <ul>
        <li
          class="destination-list-element"
          v-for="item in destination"
          :value="item.value"
          :key="item.value"
        >
          <div class="top-destination-content">
            <h2>{{ item.name }}</h2>
            <span>
              <a :href="'http://localhost:8080/#/destination/edit/' + item.id">Edit</a>
              <a v-on:click="deleteDestination(item.id)">Delete</a>
            </span>
          </div>

          <ul class="horizontal-details">
            <li>
              <p>
                <strong>COUNTRY:</strong>
                {{ item.id }}
              </p>
            </li>
            <li>
              <p>
                <strong>TYPE:</strong>
                {{ item.destination_type }}
              </p>
            </li>
            <li>
              <p>
                <strong>DISTRICT:</strong>
                {{ item.district }}
              </p>
            </li>
            <li>
              <p>
                <strong>LATITUDE:</strong>
                {{ item.crd_latitude }}
              </p>
            </li>
            <li>
              <p>
                <strong>LONGITUDE:</strong>
                {{ item.crd_longitude }}
              </p>
            </li>
          </ul>

          <div class="destination-container">
            <div class="image-container">
              <img
                src="https://images.pexels.com/photos/371633/pexels-photo-371633.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
              >
            </div>
            <div class="info-container">
              <img src="http://humaan.com/wp-content/uploads/2014/11/google-maps-basic-marker.png">
              <!-- <ul >
                    <li> <p><strong>COUNTRY: </strong>{{ item.country }}</p></li>
                    <li><p><strong>TYPE: </strong>{{ item.destination_type }}</p></li>
                    <li><p><strong>DISTRICT: </strong>{{ item.district }}</p></li>
                    <li> <p><strong>LATITUDE: </strong>{{ item.crd_latitude }}</p></li>
                    <li><p><strong>LONGITUDE: </strong>{{ item.crd_longitude }}</p></li>
              </ul>-->
            </div>
          </div>
          <!-- <hr> -->
        </li>
      </ul>
    </div>
  </div>
</template>

<style>
@import url("https://fonts.googleapis.com/css?family=Karla:400,700");

.outer-container {
  text-align: center;
}

.destination-container {
  display: flex;
}

.destination-container .image-container {
  width: 70%;
}

.destination-container .info-container {
  width: 30%;
}

.destination-container img {
  height: 200px;
  object-fit: cover;
  width: 100%;
}

/* .destination-container .image-container img{
    height: 200px;
    object-fit: cover;
    width: 100%;
} */

.horizontal-details {
  padding-top: 15px;
  background-color: #05386b;
}

.horizontal-details li {
  display: inline-block;
  padding-right: 20px;
  color: white;
}

.container {
  margin: 10px 100px;
  align-self: center;
  display: inline-block;
  text-align: left;
}

.top-destination-content {
  background-color: #edf5e1;
  display: flex;
  justify-content: space-between;
}

.top-destination-content span {
  padding: 10px 20px;
}

.top-destination-content a {
  font-family: "Karla", sans-serif;
  font-size: 15px;
  margin-left: 10px;
}

h2 {
  font-family: "Karla", sans-serif;
  font-size: 25px;
  padding: 20px 20px;
  /* background-color: #dfdce3; */
}

.banner {
  height: 300px;
  width: 100%;

  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url("https://gallery.yopriceville.com/var/albums/Backgrounds/Autumn_Landscape_Background.jpg?m=1442666745");
  background-position: center;
  /* opacity: 0.6; */
}

.banner h1 {
  font-family: "Karla", sans-serif;
  text-align: center;
  color: white;
  padding-top: 60px;
  font-size: 65px;
  font-weight: bold;
}

.banner hr {
  margin: 10px 200px;
  margin-top: 30px;
  color: white;
  opacity: 0.5;
}

.test {
  border: 1px solid black;
}

ul {
  list-style: none;
}

.destination-list-element {
  padding-top: 20px;
}
</style>


<script>
import { RepositoryFactory } from "../../repository/RepositoryFactory";
let destinationRepostory = RepositoryFactory.get("destination");

export default {
  data() {
    return {
      destination: []
    };
  },
  methods: {
    deleteDestination: function(id) {
      for (var i = 0; i < this.destination.length; i++) {
        if (this.destination[i].id === id) {
          this.destination.splice(i, 1);
        }
      }
      destinationRepostory.deleteDestination(id).then(result => {
        console.log(result);
      });
      
    }
  },
  created: function() {
    destinationRepostory.getDestination().then(result => {
      this.destination = result;
    });
  }
};
</script>
