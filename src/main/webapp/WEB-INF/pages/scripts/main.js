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
        $contentButton,
        $workshop;




    function initVars() {

        statisticEmpty = true;

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
        var i, j;

        // Removing content
        $("." + classes.workshopQueueItem).remove();
        $("." + classes.workshopMastersItem).remove();



        if (workshops) {
            for (i = 0; i < workshops.length; ++i) {
                var id = workshops[i].id;

                var markupWorkshop = $("#workshop" + id);

                // Update queue
                var queue = workshops[i].queue;
                var markupQueue = markupWorkshop.find("." + classes.workshopQueue);
                for (j = 0; j < queue.length; ++j) {
                    markupQueue.append(getQueueItemMarkup(queue[j].id, queue[j].number, queue[j].service, queue[j].date));
                }

                //Update masters
                var masters = workshops[i].masters;
                var markupMasters = markupWorkshop.find("." + classes.workshopMasters);
                for (j = 0; j < masters.length; ++j) {
                    markupMasters.append(getMasterMarkup(masters[j].id, masters[j].name, masters[j].busy));
                }
            }
        }
    }



    function updateStatistic(statistic) {
        var key, i;

        $("#averageTime").html(statistic.averageTime);
        $("#income").html(statistic.income);
        $("#servedCar").html(statistic.servedCar);

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

    function getQueueItemMarkup(id, number, service, date) {
        return '<li class="workshop-queue__item" id="ticket' + id + '">' +
            '<span class="workshop-queue__item-number">' + number + '</span>' +
            '<span class="workshop-queue__item-service">' + service + '</span>' +
            '<span class="workshop-queue__item-date">' + date + '</span>' +
        '</li>';
    }

    function getMasterMarkup(id, name, busy) {
        function busyMarkup(busy) {
            if(busy) {
                return '<span class="workshop-masters__item-work">занят</span>';
            } else {
                return '<span class="workshop-masters__item-work workshop-masters__item-work_free">свободен</span>';
            }
        }

        return '<li class="workshop-masters__item" + id="master' + id + '">' +
            '<span class="workshop-masters__item-name">' + name + '</span>' +
            + busyMarkup(busy) +
            '</li>';

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