var liveTiming = angular.module("liveTiming", []);

var PRACTICE = 0; var P_STRING = "практика";
var QUALIFY  = 1; var Q_STRING = "квалификация";
var RACE     = 2; var R_STRING = "гонка";
var N_STRING = "нет";
var UPDATE_INTERVAL = 1000; // интервал обновления данных в миллисекундах

var RECENT_TIME_DELTA = 8000;

// TODO: функционал по вычислению лучшего круга гонки должен быть на сервере

liveTiming.controller('LiveTimingCtrl', ['$scope', '$interval', '$http', function($scope, $interval, $http) {
	$interval(function() {
		$http.get('/ACTiming2/live?brief=0').success(function(data) {
			$scope.timingData = data;
			$scope.sessionOk = true;
			$scope.isRace = false;       
			$scope.lapsRemaining = 0;    // оставшееся количество кругов в гонке
			// Если время сессии закончилось, его отсчёт продолжается в минус. Это пригодится при расчёте 
			// "свежести" круга в квалификации. На странице же нужно отобразить ноль.
			$scope.timeLeftView = ($scope.timingData.timeLeft < 0)? 0 : $scope.timingData.timeLeft;
			switch (data.session) {
			case PRACTICE: {
				data.sessionString = P_STRING;
				break;
			}
			case QUALIFY: {
				data.sessionString = Q_STRING;
				break;
			}
			case RACE: {
				data.sessionString = R_STRING;
				$scope.isRace = true;
				// Рассчитываем число оставшихся кругов относительно лидера. Если лидер пересёк финишную черту,
				// текущий круг станет последним для всех, в том числе и круговых.
				$scope.lapsRemaining = $scope.timingData.numberOfLaps - $scope.timingData.cars[0].lapsCompleted
				break;
			}
			default: {
				data.sessionString = N_STRING;
				$scope.sessionOk = false;
				break;
			}
			}
			if ($scope.isRace) {
				/* Здесь установим лучший круг гонки.
				 * Если завершён первый круг, то лучшее время круга равно времени круга лидера.
				 * Если завершён не первый круг, то сравниваем все времена кругов (lastLap) с предыдущим лучшим;
				 * если время улучшено, запоминаем данные. */
				$scope.bestLapIndex = -1;  // Временный показатель. Равенство -1 означает, что лучшее время круга не было побито.
				if (data.cars[0].lapsCompleted == 1) {  // FIXME Лидер может поменяться во время второго круга 
					$scope.bestLap = data.cars[0].bestLap; 
					$scope.bestLapIndex = 0;
				} else if (data.cars[0].lapsCompleted > 1)
					for (i = 0; i < data.cars.length; i++)
						if (data.cars[i].bestLap != 0 && data.cars[i].bestLap < $scope.bestLap)
							$scope.bestLapIndex = i;
				if ($scope.bestLapIndex > -1) {
					$scope.bestLap = data.cars[$scope.bestLapIndex].bestLap;
					$scope.bestLapDriver = data.cars[$scope.bestLapIndex].driverName;
					$scope.bestLapNumber = data.cars[$scope.bestLapIndex].lapsCompleted;
				}
				console.log("Race info: lap " + data.cars[0].lapsCompleted + "; bestLapIndex = " + $scope.bestLapIndex);
			}
		});
	}, UPDATE_INTERVAL);
	
	// Возвращает true, если поставленное время достаточно свежее: отметка времени для поставленного круга отличается от оставшегося времени сессии
	// не более, чем на RECENT_TIME_DELTA миллисекунд. Используется при оформлении и имеет смысл для практики и квалификации. 
	$scope.isRecentLap = function(time, lastLap) {
		return (time - $scope.timingData.timeLeft < RECENT_TIME_DELTA) && ($scope.timingData.session != RACE) && (lastLap > 0);
	}
	
	// Возвращает true, пилот находится в одном круге с лидером. Имеет смысл только для гонки.
	$scope.isLeadLap = function(lapNumber) {
		return (lapNumber == $scope.timingData.cars[0].lapsCompleted) && $scope.timingData.session == RACE;
	}
	
	$scope.isBestLap = function(lapTime) {
		return lapTime != 0 && lapTime == $scope.bestLap;
	}
	
	$scope.isBestLapAvailable = function() {
		var result = $scope.isRace && $scope.timingData.cars[0].lapsCompleted > 0;
		console.log("isBestLapAvailable: " + result);
		return result;
	}
	
}]);