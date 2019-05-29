<%@page contentType="text/html" pageEncoding="UTF-8" %>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Formula Search</title>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML"></script>
  <script type="text/x-mathjax-config">
          <%@include file="../js/mathJax.js"%>
      </script>

  <script src='http://code.jquery.com/jquery-3.1.1.min.js'></script>
  <script charset="UTF-8">
      <%@include file="../js/search.js"%>
  </script>
  <style>
    <%@include file="../css/Index.css"%>
  </style>
  <script type="application/javascript">
      MathJax.Hub.Config({
          showProcessingMessages: false,
          tex2jax: {
              inlineMath: [
                  ['$', '$'],
                  ['\\(', '\\)']
              ]
          },
          jax: ["input/TeX","output/HTML-CSS"],
          // displayAlign: "left",
          extensions: ['toMathML.js']
      });
  </script>
</head>
<body>
<h1 id="title">Formula Search in Wikipedia</h1>
<div id="input-div">
  <label for="math-input">
  </label>
  <textarea id="math-input" oninput="updateMath(this.value)"></textarea>
  <select id="selection" size="1">
    <option selected value="term">По терминам</option>
    <option value="formula">По фрагменту</option>
  </select>
  <button id="btn-search" onclick="search()">Search</button>

</div>
<div id="output-div">
  <div id="MathOutput">$$\{}$$</div>
</div>
<label for="output">
</label>
<textarea id="output" cols="100" rows="10" readonly="readonly"></textarea>

<div id="content">

</div>

</body>
</html>
