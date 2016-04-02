$(document).ready(function() {

    var classes = {
        contentButton: "content__button",
        contentButtonPlay: "content__button_play",
        workshop: "workshop",
        workshopMasters: "workshop-masters",
        workshopMastersItem: "workshop-masters__item",
        workshopQueue: "workshop-queue",
        workshopQueueItem: "workshop-queue__item"
    };



    var constants = {
        TIMER_DELAY: 5000
    };



    var timerId,
        statisticEmpty,
        workshopsEmpty,
        $contentButton,
        $workshop;




    function initVars() {

        statisticEmpty = true;
        workshopsEmpty = true;

        $contentButton = $("." + classes.contentButton);
        $workshop = $("." + classes.workshop);
    }


    function bindEvents() {
        $contentButton.on("click", playButtonHandler);
    }



    function playButtonHandler() {
        var $this = $(this);
        if ($this.hasClass(classes.contentButtonPlay)) {
            $this.removeClass(classes.contentButtonPlay);
            timerStart();
        } else {
            $this.addClass(classes.contentButtonPlay);
            clearTimeout(timerId);
        }
    }



    function updateContent(data) {
        if (data) {
            updateWorkshops(data.workshops);
            updateStatistic(data.statistic);
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



    function updateStatistic(statistic) {
        var i;

        $("#averageTime").html(statistic.averageTime);
        $("#profit").html(statistic.profit);
        $("#servedCar").html(statistic.servedCarCount);

        var queueLength = statistic.queueLength;
        for (i = 0; i < queueLength.length; ++i) {
            $("#queueLength" + queueLength[i].id).html(queueLength[i].length);
        }

        var salaries = statistic.salaries;
        var servicesNumber = statistic.servicesNumber;


        if (statisticEmpty) {
            statisticEmpty = false;

            for (i = 0; i < salaries.length; ++i) {
                $("#salaries").append(getStatisticBlockMarkup("masterSalary" + salaries[i].id, salaries[i].name, salaries[i].salary));
            }

            for (i = 0; i < servicesNumber.length; ++i) {
                $("#servicesNumber").append(getStatisticBlockMarkup("serviceNumber" + servicesNumber[i].id, servicesNumber[i].name, servicesNumber[i].number));
            }

        } else {
            for (i = 0; i < salaries.length; ++i) {
                $("#masterSalary" + salaries[i].id).html(salaries[i].salary);
            }

            for (i = 0; i < servicesNumber.length; ++i) {
                $("#serviceNumber" + servicesNumber[i].id).html(servicesNumber[i].number);
            }
        }
    }



    function getStatisticBlockMarkup(id, param, value) {
    // Helpers
        return '<div class="statistic__block">' +
            '<div class="statistic__param">' + param + '</div>' +
            '<div class="statistic__value" id="' + id + '">' + value + '</div>' +
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
            timerId = setTimeout(tick, constants.TIMER_DELAY);
        }, constants.TIMER_DELAY);
    }


    function startQueueThread() {
        $.ajax({
            method: 'GET',
            url: '/startQueueThread',
            data: {},
            success: function(data) {
                console.log("Queue generating is started");
            }
        });
    }

    function init() {

        initVars();

        bindEvents();

        startQueueThread();

        receive();
        timerStart();
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