/**
 * Created by obi on 6/27/14.
 */
var service = angular.module('starter.services', []);

service.factory('utils', function($ionicLoading, $timeout){
    return {
        startup : function(scope){
            var success = function(msg){
                var result = JSON.parse(msg);
                var books = {
                    OT : result.slice(0, 39),
                    NT : result.slice(39, 65)
                };
                scope.otActive = true;
                scope.ntActive = true;
                scope.books = books;
                scope.$apply();
                scope.setCurrentBook(scope.books.OT[0]);
                scope.$apply();
                $ionicLoading.hide();
            };
            var fail = function(e){
                alert(e);
            };
            var interval = setInterval(function(){
                if(cordova){
                    clearInterval(interval);
                    cordova.exec(success, fail, 'JavaBridge', 'getBooks', []);
                }
            }, 1000);
        },
        setCurrentBook : function(scope, book, chapter, override){
            if(scope.currentBookName === book && !override){
                return;
            }

            $ionicLoading.show({
                template: '<a class="ion-loading-d"></a>'
            });
            scope.currentBookName = book;
            var success = function(msg){
                scope.currentBook = JSON.parse(msg);
                scope.$apply();
                if(chapter){
                    scope.currentChapter = chapter;
                }else{
                    scope.currentChapter = 1;
                }
                scope.$apply();
                scope.chapterList = scope.getChapters(8);
                scope.$apply();
                $timeout(function(){
                    $ionicLoading.hide();
                }, 1000, false);
            };
            var fail = function(e){
                alert(e);
            };
            $timeout(function(){
                scope.currentBook = null;
                scope.$apply();
                cordova.exec(success, fail, 'JavaBridge', 'getBook', [book, scope.bookType.book]);
            }, 500, false);
        },
        getChapters : function(scope, division){
            var seg = scope.currentBook.chapterList.length / division;
            var chapters = scope.currentBook.chapterList;
            var arrangedChapters = [];
            for(var i = 0; i < seg; i++){
                var sub = [];
                for(var j = 0; j < division; j++){
                    if(chapters[division * i + j]){
                        sub[j] = chapters[division * i + j];
                    }
                }
                arrangedChapters.push(sub);
            }
            return arrangedChapters;
        },
        changeChapter : function(scope, dir){
            if(dir === 'prev'){
                var currentPos = scope.currentChapter;
                scope.currentChapter = currentPos - 1 > 0? currentPos - 1 : currentPos;
            }else{
                var currentPos = scope.currentChapter;
                scope.currentChapter = currentPos < scope.currentBook.chapterList.length? currentPos + 1 : currentPos;
            }
        }
    }
});

service.factory('bookmarkService', function(){
    var success = function(scope){
        scope.update();
    };
    var fail = function(){

    };

    return {
        addBookmark : function(scope, bookmarkObject){

        },
        getBookmarks : function(scope){

        }
    }
});

service.factory('searchService', function(){
    var success = function(scope){
        scope.update();
    };
    var fail = function(){

    };

    return {
        search : function(scope, searchObject){

        }
    }
});