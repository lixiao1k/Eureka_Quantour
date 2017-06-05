<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>

    <meta charset="UTF-8">
    <meta name="Generator" content="EditPlus®">
    <meta name="Author" content="">
    <meta name="Keywords" content="">
    <meta name="Description" content="">
    <title>cna</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js">
    </script>
    <script src="scripts/recordPaint.js"></script>
    <style>

    </style>
</head>
<script>
    $(function(){

        var datas = [1200,2000,3000,500,200,800,1800,2200,2600,1000,600,300];
        recordPaint($("#recordCvs")[0],datas);
    });
</script>
<body>



<div id="recordContent">
    <canvas id="recordCvs" width="600" height="400"></canvas>
</div>
</body>
</html>