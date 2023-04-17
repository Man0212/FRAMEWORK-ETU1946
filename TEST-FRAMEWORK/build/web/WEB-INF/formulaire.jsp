<!DOCTYPE html>
<html>
  <head>
    <title>Quote Request Form</title>
    <style>
      /* Form styling */
      form {
        width: 50%;
        margin: 0 auto;
        padding: 30px;
        background-color: #f1f1f1;
        border: 1px solid #ccc;
      }

      /* Form input styling */
      input[type="text"],
      select {
        width: 100%;
        padding: 12px 20px;
        margin: 8px 0;
        box-sizing: border-box;
        border: 1px solid #ccc;
        border-radius: 4px;
      }

      /* Form label styling */
      label {
        font-size: 18px;
        margin-bottom: 8px;
        display: block;
      }

      /* Form submit button styling */
      input[type="submit"] {
        width: 100%;
        background-color: #1a1f76;
        color: white;
        padding: 14px 20px;
        margin: 8px 0;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }

      /* Form submit button on hover */
      input[type="submit"]:hover {
        background-color: #1a1f36;
      }
    </style>
  </head>
  <body>
    <form action="affiche" method="get">
     <p>nom  <input type="text" id="nom" name="nom"></p>
      <p>age <input type="text" id="age" name="age"></p>
      <input type="submit" value="Submit Quote Request">
    </form>
  </body>
</html>
