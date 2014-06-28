angular.module('starter.controllers', [])

.controller('bookViewCtrl', function($scope, $ionicGesture, $ionicScrollDelegate){
    $scope.selectItem = function(index){
        if($scope.selectedItem === index){
            $scope.selectedItem = -1;
            return;
        }
        $scope.selectedItem = index;
    };

    $scope.share = function(verse){
        alert('sharing ' + $scope.currentBookName + ':' + $scope.currentChapter + ':' + verse);
    };

    $scope.addBookmark = function(verse){
        alert('bookmarking ' + $scope.currentBookName + ':' + $scope.currentChapter + ':' + verse);
    };

    $scope.$watch('currentChapter', function(){
        $scope.selectedItem = -1;
    });

    $ionicScrollDelegate.$getByHandle('contentList').forgetScrollPosition();
})

.controller('AppCtrl', function($scope, $state, $ionicLoading, utils,
                                 $ionicGesture, $ionicSideMenuDelegate, $timeout){
        $scope.bookType = {
            book : 'KJV',
            books : ['KJV', 'WEV', 'NHEB'],
            booksMap : ['King James Version', 'World English Version', 'New Hearts Edition']
        };
        $ionicLoading.show({
            template: 'Loading...'
        });
        $scope.$on('$viewContentLoaded', function(){
            utils.startup($scope);
        });

        $scope.setCurrentBook = function(book, chapter, override){
            utils.setCurrentBook($scope, book, chapter, override);
        };

        $ionicGesture.on('drag', function(){
            $ionicSideMenuDelegate.toggleLeft(true);
        }, angular.element(document.getElementById('drag')));

        $scope.setChapter = function(val){
            $scope.currentChapter = val;
        };

        $scope.$watch('bookType.book', function(newVal, oldVal){
           $scope.setCurrentBook($scope.currentBookName, $scope.currentChapter, true);
        });

        $scope.getChapters = function(division){
            return utils.getChapters($scope, division);
        };

        $scope.changeChapter = function(dir){
            utils.changeChapter($scope, dir);
        };
});