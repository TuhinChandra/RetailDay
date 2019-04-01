window.onload = function() {
	loadEventsRatings();
}

function loadEventsRatings() {
	var URL = '';
	var xhr = new XMLHttpRequest();
	xhr.open('GET', URL + '/getVoteCountForAllEvents');
	xhr.onload = function() {
		if (xhr.status === 200) {
			console.log("success=" + xhr.responseText);

			var rating_array = JSON.parse(xhr.responseText);

			var likeArray1 = [];
			var dislikeArray1 = [];

			var likeArray2 = [];
			var dislikeArray2 = [];

			var likeArray3 = [];
			var dislikeArray3 = [];

			var noOfEventsPerChart = 6;
			var eventIndex = 0;
			for (; eventIndex < noOfEventsPerChart; eventIndex++) {
				likeArray1.push({
					"label" : rating_array[eventIndex].itemId,
					"y" : rating_array[eventIndex].likeCount
				});
				dislikeArray1.push({
					"label" : rating_array[eventIndex].itemId,
					"y" : rating_array[eventIndex].dislikeCount
				});
			}
			for (; eventIndex < noOfEventsPerChart * 2; eventIndex++) {
				likeArray2.push({
					"label" : rating_array[eventIndex].itemId,
					"y" : rating_array[eventIndex].likeCount
				});
				dislikeArray2.push({
					"label" : rating_array[eventIndex].itemId,
					"y" : rating_array[eventIndex].dislikeCount
				});
			}

			for (; eventIndex < (noOfEventsPerChart * 3) - 1; eventIndex++) {
				likeArray3.push({
					"label" : rating_array[eventIndex].itemId,
					"y" : rating_array[eventIndex].likeCount
				});
				dislikeArray3.push({
					"label" : rating_array[eventIndex].itemId,
					"y" : rating_array[eventIndex].dislikeCount
				});
			}

			var chart1 = new CanvasJS.Chart("chart1Container", {
				animationEnabled : true,
				title : {
					text : "Kingfisher day, 2018 - Event Ratings"
				},
				axisY : {
					title : "Vote Counts",
					titleFontColor : "#4F81BC",
					lineColor : "#4F81BC",
					labelFontColor : "#4F81BC",
					tickColor : "#4F81BC"
				},
				toolTip : {
					shared : true
				},
				legend : {
					cursor : "pointer",
					itemclick : toggleDataSeries1
				},
				data : [ {
					type : "column",
					name : "Loves from Associates",
					legendText : "Loves",
					showInLegend : true,
					dataPoints : likeArray1
				}, {
					type : "column",
					name : "Likes from Associates",
					legendText : "Likes",
					axisYType : "secondary",
					showInLegend : true,
					dataPoints : dislikeArray1
				} ]
			});

			var chart2 = new CanvasJS.Chart("chart2Container", {
				animationEnabled : true,
				title : {
					text : "Kingfisher day, 2018 - Event Ratings"
				},
				axisY : {
					title : "Vote Counts",
					titleFontColor : "#4F81BC",
					lineColor : "#4F81BC",
					labelFontColor : "#4F81BC",
					tickColor : "#4F81BC"
				},
				toolTip : {
					shared : true
				},
				legend : {
					cursor : "pointer",
					itemclick : toggleDataSeries2
				},
				data : [ {
					type : "column",
					name : "Loves from Associates",
					legendText : "Loves",
					showInLegend : true,
					dataPoints : likeArray2
				}, {
					type : "column",
					name : "Likes from Associates",
					legendText : "Likes",
					axisYType : "secondary",
					showInLegend : true,
					dataPoints : dislikeArray2
				} ]
			});

			var chart3 = new CanvasJS.Chart("chart3Container", {
				animationEnabled : true,
				title : {
					text : "Kingfisher day, 2018 - Event Ratings"
				},
				axisY : {
					title : "Vote Counts",
					titleFontColor : "#4F81BC",
					lineColor : "#4F81BC",
					labelFontColor : "#4F81BC",
					tickColor : "#4F81BC"
				},
				toolTip : {
					shared : true
				},
				legend : {
					cursor : "pointer",
					itemclick : toggleDataSeries3
				},
				data : [ {
					type : "column",
					name : "Loves from Associates",
					legendText : "Loves",
					showInLegend : true,
					dataPoints : likeArray3
				}, {
					type : "column",
					name : "Likes from Associates",
					legendText : "Likes",
					axisYType : "secondary",
					showInLegend : true,
					dataPoints : dislikeArray3
				} ]
			});

			chart1.render();
			chart2.render();
			chart3.render();

			function toggleDataSeries1(e) {
				if (typeof (e.dataSeries.visible) === "undefined"
						|| e.dataSeries.visible) {
					e.dataSeries.visible = false;
				} else {
					e.dataSeries.visible = true;
				}
				chart1.render();
			}

			function toggleDataSeries2(e) {
				if (typeof (e.dataSeries.visible) === "undefined"
						|| e.dataSeries.visible) {
					e.dataSeries.visible = false;
				} else {
					e.dataSeries.visible = true;
				}
				chart2.render();
			}

			function toggleDataSeries3(e) {
				if (typeof (e.dataSeries.visible) === "undefined"
						|| e.dataSeries.visible) {
					e.dataSeries.visible = false;
				} else {
					e.dataSeries.visible = true;
				}
				chart3.render();
			}

			document.getElementById("loader").style.display = "none";

		} else {
			alert('Request failed.  Returned status of ' + xhr.status);
		}
	};
	xhr.send();
}

function showPage() {
	document.getElementById("loader").style.display = "none";
	document.getElementById("myDiv").style.display = "block";
}