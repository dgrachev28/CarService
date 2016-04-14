$(document).ready(function() {

    var classes = {
        workshop: "workshop",
        workshopMasters: "workshop-masters",
        workshopMastersItem: "workshop-masters__item",
        workshopQueue: "workshop-queue",
        workshopQueueItem: "workshop-queue__item",
        disabled: "_disabled"
    };


    var id = {
        settingsButton: "settings-button",
        startButton: "start-button",
        pauseButton: "pause-button",
        stopButton: "stop-button"
    };



    var timerId,
        timerDelay,
        statisticEmpty,
        workshopsEmpty,
        $contentButton,
        $workshop,
        $settingsButton,
        $startButton,
        $pauseButton,
        $stopButton,
        averageQueueLength,

        //RUNNING / STOPPED / PAUSED
        processState;




    function initVars() {

        timerDelay = extractOneSetting($("#settings-step"));

        statisticEmpty = true;
        workshopsEmpty = true;

        processState = "RUNNING";

        $contentButton = $("." + classes.contentButton);
        $workshop = $("." + classes.workshop);
        $settingsButton = $("#" + id.settingsButton);
        $startButton = $("#" + id.startButton);
        $pauseButton = $("#" + id.pauseButton);
        $stopButton = $("#" + id.stopButton);

        averageQueueLength = {
            iterationsNumber: 0,
            queueLength1: 0,
            queueLength2: 0,
            queueLength3: 0,
            queueLength4: 0
        };
    }


    function bindEvents() {
        $pauseButton.on("click", pauseClickHandler);
        $settingsButton.on("click", settingsClickHandler);
        $startButton.on("click", startClickHandler);
        $stopButton.on("click", stopClickHandler);
    }



    function pauseClickHandler() {
        if (!$(this).hasClass(classes.disabled)) {
            if (processState == "PAUSED") {
                processState = "RUNNING";
                timerStart();
            } else {
                processState = "PAUSED";
                clearTimeout(timerId);
            }
        }
    }


    function settingsClickHandler(e) {
        if($(e.target).hasClass("setting-panel__button_settings")) {
            $settingsButton.toggleClass("_opened");
        }
    }

    function startClickHandler() {
        startApplication();
    }


    function stopClickHandler() {
        if (!$(this).hasClass(classes.disabled)) {
            stopApplication();
        }
    }


    function updateContent(data) {
        if (data) {
            updateWorkshops(data.workshops);
            countAverageQueueLength(data.workshops);
            updateStatistics(data.statistics);
            updateTime(data.currentDateTime);
        }
    }



    function countAverageQueueLength(workshops) {
        var i, id, newValue, oldValue, currentQueueLength, iterationsNumber;

        for (i = 0; i < workshops.length; ++i) {
            id = workshops[i].id;
            currentQueueLength = workshops[i].queue.length;
            oldValue = averageQueueLength["queueLength" + id];
            iterationsNumber = averageQueueLength.iterationsNumber;

            newValue = (oldValue * iterationsNumber + currentQueueLength) / (iterationsNumber + 1);

            averageQueueLength["queueLength" + id] = newValue;

            ++averageQueueLength.iterationsNumber;

        }
    }


    function updateWorkshops(workshops) {
        var i, j, id, markupWorkshop, masters, markupMasters;

        // Update masters

        if (workshopsEmpty) {
            workshopsEmpty = false;

            for (i = 0; i < workshops.length; ++i) {
                id = workshops[i].id;

                markupWorkshop = $("#workshop" + id);

                //Add masters
                masters = workshops[i].masters;
                markupMasters = markupWorkshop.find("." + classes.workshopMasters);
                markupMasters.html("");

                for (j = 0; j < masters.length; ++j) {
                    markupMasters.append(getMasterMarkup(masters[j].id, masters[j].name, masters[j].busy));
                }
            }
        } else {

            for (i = 0; i < workshops.length; ++i) {

                //Update masters
                masters = workshops[i].masters;
                for (j = 0; j < masters.length; ++j) {
                    if (masters[j].busy) {
                        $("#master" + masters[j].id).html(
                            '<span class="workshop-masters__item-name">' + masters[j].name + '</span>' +
                            '<span class="workshop-masters__item-work">занят</span>'
                        );
                    } else {
                        $("#master" + masters[j].id).html(
                            '<span class="workshop-masters__item-name">' + masters[j].name + '</span>' +
                            '<span class="workshop-masters__item-work workshop-masters__item-work_free">свободен</span>'
                        );
                    }
                }
            }
        }


        //Update workshops

        for (i = 0; i < workshops.length; ++i) {
            id = workshops[i].id;

            markupWorkshop = $("#workshop" + id);

            // Update queue
            var queue = workshops[i].queue;
            var markupQueue = markupWorkshop.find("." + classes.workshopQueue);

            //Clear queues
            markupQueue.html("");

            for (j = 0; j < queue.length; ++j) {
                var date = new Date(queue[j].addQueueDate);
                markupQueue.append(getQueueItemMarkup(queue[j].id, queue[j].client.carId, queue[j].service.name,
                                    date.customFormat("#D# #MMM# #YYYY# &nbsp; &nbsp; &nbsp; #hhh#:#mm#"), queue[j].status));
            }
        }
    }



    function updateStatistics(statistics) {
        var i;

        $("#averageTime").html(statistics.averageTime);
        $("#profit").html(statistics.profit);
        $("#servedCar").html(statistics.servedCarCount);

        for(i = 1; i <= 4; ++i) {
            $("#queueLength" + i).html(Math.round(averageQueueLength["queueLength" + i]));
        }

        var mastersIncome = statistics.mastersIncome;
        var servicesNumber = statistics.servicesNumber;
        var key;

        $("#salaries").html("");
        $("#servicesNumber").html("");

        for (key in mastersIncome) {
            if (mastersIncome.hasOwnProperty(key)) {
                $("#salaries").append(getStatisticBlockMarkup(key, mastersIncome[key]));
            }
        }

        for (key in servicesNumber) {
            if (servicesNumber.hasOwnProperty(key)) {
                $("#servicesNumber").append(getStatisticBlockMarkup(key, servicesNumber[key]));
            }
        }
    }



    function updateTime(currentDateTime) {
        var date = new Date(currentDateTime);
        $("#current-time").html(date.customFormat("#D# #MMM# #YYYY# &nbsp; &nbsp; &nbsp; #hhh#:#mm#"));
    }


    function clearMarkup() {
        $("." + classes.workshopMasters).html("");
        $("." + classes.workshopQueue).html("");
        $("#averageTime").html("");
        $("#profit").html("");
        $("#servedCar").html("");
        $("#queueLength1").html("");
        $("#queueLength2").html("");
        $("#queueLength3").html("");
        $("#queueLength4").html("");
        $("#salaries").html("");
        $("#servicesNumber").html("");
    }


    function getStatisticBlockMarkup(param, value) {
    // Helpers
        return '<div class="statistic__block">' +
            '<div class="statistic__param">' + param + '</div>' +
            '<div class="statistic__value">' + value + '</div>' +
            '</div>';
    }

    function getStatusMarkup(status) {
        if (status == "InProcess") {
            return 'workshop-queue__item_processing';
        } else {
            return '';
        }
    }

    function getQueueItemMarkup(id, number, service, date, status) {
        return '<li class="workshop-queue__item ' + getStatusMarkup(status) + ' id="ticket' + id + '">' +
            '<span class="workshop-queue__item-number">' + number + '</span>' +
            '<span class="workshop-queue__item-service">' + service + '</span>' +
            '<span class="workshop-queue__item-date">' + date + '</span>' +
        '</li>';
    }

    function getMasterMarkup(id, name, busy) {
        if(busy) {
            return '<li class="workshop-masters__item" + id="master' + id + '">' +
                    '<span class="workshop-masters__item-name">' + name + '</span>' +
                    '<span class="workshop-masters__item-work">занят</span>' +
                    '</li>';
        } else {
            return '<li class="workshop-masters__item" + id="master' + id + '">' +
                '<span class="workshop-masters__item-name">' + name + '</span>' +
                '<span class="workshop-masters__item-work workshop-masters__item-work_free">свободен</span>' +
                '</li>';
        }
    }


    function extractOneSetting(element) {
        if (element.hasClass("_error")) {
            return +element.attr("defaultValue");
        } else {
            return +element.val();
        }
    }

    function extractSettings() {
        var settings = {};
        settings.masterCount1 = extractOneSetting($("#settings-masters1"));
        settings.masterCount2 = extractOneSetting($("#settings-masters2"));
        settings.masterCount3 = extractOneSetting($("#settings-masters3"));
        settings.masterCount4 = extractOneSetting($("#settings-masters4"));
        settings.minIntervalMinutes = extractOneSetting($("#settings-min-interval"));
        settings.maxIntervalMinutes = extractOneSetting($("#settings-max-interval"));
        settings.timeCoefficient = extractOneSetting($("#settings-coefficient"));
        return settings;
    }



    // Получение данных с сервера
    function receive() {
        $.ajax({
            method: 'GET',
            url: '/getCurrentState',
            accepts: "application/json; charset=utf-8",
            data: {},
            success: function(data) {
                updateContent(data);
            }
        });
    }


    // Запуск таймера
    function timerStart() {
        timerId = setTimeout(function tick() {
            receive();
            timerId = setTimeout(tick, timerDelay);
        }, timerDelay);
    }


    function startApplication() {
        $.ajax({
            method: 'GET',
            url: '/startApplication',
            data: extractSettings(),
            success: function(data) {
                initVars();
                timerStart();
            }
        });
    }


    function stopApplication(callback) {
        $.ajax({
            method: 'GET',
            url: '/stopApplication',
            data: {},
            success: function() {
                if(callback) {
                    clearMarkup();
                    processState = "STOPPED";
                    callback();
                }
            }
        });
    }

    function init() {

        initVars();

        bindEvents();

        startApplication();
    }


    // Начало работы
    init();

});





Date.prototype.customFormat = function(formatString){
    var YYYY,YY,MMMM,MMM,MM,M,DDDD,DDD,DD,D,hhhh,hhh,hh,h,mm,m,ss,s,ampm,AMPM,dMod,th;
    YY = ((YYYY=this.getFullYear())+"").slice(-2);
    MM = (M=this.getMonth()+1)<10?('0'+M):M;
    MMM = (MMMM=["January","February","March","April","May","June","July","August","September","October","November","December"][M-1]).substring(0,3);
    DD = (D=this.getDate())<10?('0'+D):D;
    DDD = (DDDD=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"][this.getDay()]).substring(0,3);
    th=(D>=10&&D<=20)?'th':((dMod=D%10)==1)?'st':(dMod==2)?'nd':(dMod==3)?'rd':'th';
    formatString = formatString.replace("#YYYY#",YYYY).replace("#YY#",YY).replace("#MMMM#",MMMM).replace("#MMM#",MMM).replace("#MM#",MM).replace("#M#",M).replace("#DDDD#",DDDD).replace("#DDD#",DDD).replace("#DD#",DD).replace("#D#",D).replace("#th#",th);
    h=(hhh=this.getHours());
    if (h==0) h=24;
    if (h>12) h-=12;
    hh = h<10?('0'+h):h;
    hhhh = h<10?('0'+hhh):hhh;
    AMPM=(ampm=hhh<12?'am':'pm').toUpperCase();
    mm=(m=this.getMinutes())<10?('0'+m):m;
    ss=(s=this.getSeconds())<10?('0'+s):s;
    return formatString.replace("#hhhh#",hhhh).replace("#hhh#",hhh).replace("#hh#",hh).replace("#h#",h).replace("#mm#",mm).replace("#m#",m).replace("#ss#",ss).replace("#s#",s).replace("#ampm#",ampm).replace("#AMPM#",AMPM);
};