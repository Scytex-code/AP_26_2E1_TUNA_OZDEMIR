<html>
<head>
<title>Catalog Report</title>
</head>

<body>

<h1>Bibliography Catalog</h1>

<table border="1">

<tr>
<th>ID</th>
<th>Title</th>
<th>Author</th>
<th>Year</th>
</tr>

<#list resources as r>

<tr>
<td>${r.id}</td>
<td>${r.title}</td>
<td>${r.author}</td>
<td>${r.year}</td>
</tr>

</#list>

</table>

</body>
</html>