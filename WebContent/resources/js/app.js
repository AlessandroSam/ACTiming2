var app = angular.module("ACTiming2", ["nvd3"]);

app.controller("sessionListCtrl", ['$scope', '$http', function($scope, $http) {
	$scope.dataReceived = false;

	$scope.plotOptions = {
            chart: {
                type: 'lineChart',
                height: 400,
                margin : {
                    top: 20,
                    right: 10,
                    bottom: 40,
                    left: 100
                },
                x: function(d) { return d.x; },
                y: function(d) { return d.y; },
                useInteractiveGuideline: true,
                dispatch: {
                    stateChange: function(e) { console.log("stateChange"); },
                    changeState: function(e) { console.log("changeState"); },
                    tooltipShow: function(e) { console.log("tooltipShow"); },
                    tooltipHide: function(e) { console.log("tooltipHide"); }
                },
                xAxis: {
                    axisLabel: 'Номер круга'
                },
                yAxis: {
                    axisLabel: 'Время круга',
                    tickFormat: function(d){
                        return d3.time.format('%M:%S.%L')(new Date(d));
                    },
                    axisLabelDistance: 20
                },
                callback: function(chart){
                    console.log("!!! lineChart callback !!!");
                }
            },
            title: {
                enable: true,
                text: 'График сессии'
            }
        };
	
    $scope.plotData = [{
    	values: [],
    	key: 'Время круга',
    	color: '#000055',
    	strokeWidth: 2
    }];

	$scope.rowClick = function(id) {
		$scope.selectedId = id;
		$http.get('/ACTiming2/laptimes?id=' + id).success(function(data) {
			$scope.bestLapIndex = 0;
			for (i = 1; i < data.laps.length; i++) {
				if (data.laps[i][1] < data.laps[$scope.bestLapIndex][1]) {					
					$scope.bestLapIndex = i;
				}
			}	
			var tmpvalues = [];			
			for (i = 0; i < data.laps.length; i++) {				
				tmpvalues.push({
					x: data.laps[i][0],
					y: data.laps[i][1]
				});
			}
			$scope.plotData[0].values = tmpvalues;			
			$scope.laps = data.laps;
			$scope.dataReceived = true;
			
		});
		
	}
	
}]);