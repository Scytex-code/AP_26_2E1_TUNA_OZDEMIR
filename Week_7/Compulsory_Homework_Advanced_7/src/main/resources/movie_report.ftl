<!DOCTYPE html>
<html>
<head>
    <title>Movie Report</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>Movie Report</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Release Date</th>
            <th>Duration</th>
            <th>Score</th>
            <th>Genres</th>
            <th>Actors</th>
        </tr>
        <#list movies as movie>
        <tr>
            <td>${movie.id}</td>
            <td>${movie.title}</td>
            <td>${movie.releaseDate}</td>
            <td>${movie.duration}</td>
            <td>${movie.score}</td>
            <td><#list movie.genres as genre>${genre.name}<#sep>, </#list></td>
            <td><#list movie.actors as actor>${actor.name}<#sep>, </#list></td>
        </tr>
        </#list>
    </table>
</body>
</html>
