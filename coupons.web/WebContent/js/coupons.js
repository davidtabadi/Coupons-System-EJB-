angular.module('CouponsApp', ['ngResource'])
  .controller('CouponsController', function($resource) {

	  var coupons = this;
	  
	  
	  coupons.invokeUrl = function (url, error, success){
		  var rsc = $resource(url);
		  
		  rsc.get(function(response){
			  if (success != null){
				  coupons.info(success);
			  }
			  else if (response.message != null){
				  coupons.info(response.message);
			  }else{
				  coupons.info('successfully invoked: ' + url);
			  }
		  }, function (){
			  if (error != null){
			  	coupons.error(error);
			  }else{
				  coupons.error("Error while invoking operation");
			  }
		  })
	  }
	  
	  coupons.info = function (message){
		  coupons.infoMessage = message;
		  coupons.errorMessage = '';
	  }

	  coupons.error = function (message){
		  coupons.infoMessage = '';
		  coupons.errorMessage = message;
	  }
	  
	  coupons.infoMessage = '';
	  coupons.errorMessage = '';	  
});
