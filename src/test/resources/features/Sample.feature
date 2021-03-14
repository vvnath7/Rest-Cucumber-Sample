Feature: Tests using Bookstore API

Background: User generates token for authorization
  Given I am an authorized user

Scenario: Authorized user is able to add or remove a book
  Given A list of books is available
  When I add a book to my reading list
  Then The book is added
  When I remove a book from my reading list
  Then The book is removed
