/**
 * Created by huihantao on 2017/6/7.
 */
function isLeapYear(year) {
    if (0 == year % 400 || (0 == year % 4 && 0 != year % 100)) {
        return true;
    }
    else {
        return false;
    }
}

function maxDayOfDate(year, month) {
    if (1 == month || 3 == month || 5 == month || 7 == month || 8 == month || 10 == month || 12 == month) {
        return 31;
    }
    else if (4 == month || 6 == month || 9 == month || 11 == month) {
        return 30;
    }
    else {
        if (isLeapYear(year)) {
            return 29;
        }
        else {
            return 28;
        }
    }
}

function getStartDate(d) {
    d.setDate(1);
    return d;
}

function getEndDate(d) {
    var totalDays = maxDayOfDate(parseInt(d.getFullYear()), parseInt(d.getMonth() + 1));
    d.setDate(totalDays);
    return d;
}

var d = new Date();
var y = d.getFullYear();
var m = d.getMonth();
var mid;

function Calender(obj) {
    this.obj = mid = obj;
    this.obj.innerHTML = this.createCalender(getStartDate(new Date(y, m)), getEndDate(new Date(y, m)));
}

Calender.prototype = {
    createCalender: function (start, end) {
        var html = "<table class='calendar' border='1px' cellpadding='0' cellspacing='0'><thead>"
            + "<tr><th colspan='7'><input type='button' value='上年' onclick='preYear()' />" + y + "<input type='button' value='上年' onclick='nextYear()' />"
            + "<input type='button' value='上月' onclick='preMonth()' />" + parseInt(m + 1) + "<input type='button' value='下月' onclick='nextMonth()' />"
            + "<input type='button' value='今天' onclick='today()'></th></tr>"
            + "<tr><th>星期一</th><th>星期二</th><th>星期三</th><th>星期四</th><th>星期五</th><th>星期六</th><th>星期日</th></tr></thead>";
        var days = end.getDate();
        var week = start.getDay() == 0 ? 7 : start.getDay();
        var tmpd = new Date();
        day = tmpd.getDate();
        for (var i = 1; i <= 42; i++) {
            if (1 == i % 7 || 1 == i) {
                html += "<tr>";
            }
            if (i >= week && i <= (week + days - 1)) {
                if (day == (i - week + 1)) {
                    if (d.getFullYear() == start.getFullYear() && d.getMonth() == start.getMonth()) {
                        html += "<td class='today'>" + day + "</td>";
                    }
                    else {
                        html += "<td >" + day + "</td>";
                    }
                } else if (day < i - week + 1) {
                    html += "<td>" + parseInt(i - week + 1) + "</td>";
                }
                else {
                    html += "<td>" + parseInt(i - week + 1) + "</td>";
                }
                if (7 == i % 7 || 7 == i) {
                    html += "</tr>";
                }
            }
            else {
                html += "<td>&nbsp;</td>";
            }
        }
        html += "</table>";
        return html;
    }
}

function preYear() {
    y = y - 1;
    new Calender(mid).createCalender(getStartDate(new Date(y, m)), getEndDate(new Date(y, m)));
}

function nextYear() {
    y = y + 1;
    new Calender(mid).createCalender(getStartDate(new Date(y, m)), getEndDate(new Date(y, m)));
}

function preMonth() {
    m = m - 1;
    if (-1 == m) {
        y = y - 1;
        m = 11;
    }
    new Calender(mid).createCalender(getStartDate(new Date(y, m)), getEndDate(new Date(y, m)));
}

function nextMonth() {
    m = m + 1;
    if (12 == m) {
        y = y + 1;
        m = 0;
    }
    new Calender(mid).createCalender(getStartDate(new Date(y, m)), getEndDate(new Date(y, m)));
}

function today() {
    y = new Date().getFullYear();
    m = new Date().getMonth();
    new Calender(mid).createCalender(getStartDate(new Date()), getEndDate(new Date()));
}

window.onload = function () {
    new Calender(document.getElementById("canlendar"));
}