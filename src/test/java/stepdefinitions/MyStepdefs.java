package stepdefinitions;

import apiEngine.Endpoints;
import apiEngine.IRestResponse;
import apiEngine.model.Book;
import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.ISBN;
import apiEngine.model.requests.RemoveBookRequest;
import apiEngine.model.responses.Books;
import apiEngine.model.responses.Token;
import apiEngine.model.responses.UserAccount;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;


public class MyStepdefs {
    private static final String USER_ID = "755579df-4cca-4241-b60d-d2058c811f0f";
    private static Response response;
    private static Token tokenResponse;
    private static IRestResponse<UserAccount> userAccountResponse;
    private static Book book;

    @Given("I am an authorized user")
    public void iAmAnAuthorizedUser() {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest("TOOLSQA-Test","Test@@123");
        tokenResponse = Endpoints.authenticateUser(authorizationRequest).getBody();
    }

    @Given("A list of books is available")
    public void aListOfBooksIsAvailable() {
        IRestResponse<Books> booksResponse = Endpoints.getBooks();
        book = booksResponse.getBody().books.get(0);
    }

    @When("I add a book to my reading list")
    public void iAddABookToMyReadingList() {
        ISBN isbn = new ISBN(book.isbn);
        AddBooksRequest addBooksRequest = new AddBooksRequest(USER_ID, isbn);
        userAccountResponse = Endpoints.addBook(addBooksRequest, tokenResponse.token);
    }

    @Then("The book is added")
    public void theBookIsAdded() {
        Assert.assertTrue(userAccountResponse.isSuccessful());
        Assert.assertEquals(201, userAccountResponse.getStatusCode());

        Assert.assertEquals(USER_ID, userAccountResponse.getBody().userID);
        Assert.assertEquals(book.isbn, userAccountResponse.getBody().books.get(0).isbn);

    }

    @When("I remove a book from my reading list")
    public void iRemoveABookFromMyReadingList() {
        RemoveBookRequest removeBookRequest = new RemoveBookRequest(USER_ID, book.isbn);
        response = Endpoints.removeBook(removeBookRequest, tokenResponse.token);
    }

    @Then("The book is removed")
    public void theBookIsRemoved() {
        Assert.assertEquals(204, response.getStatusCode());

        userAccountResponse = Endpoints.getUserAccount(USER_ID, tokenResponse.token);
        Assert.assertEquals(200, userAccountResponse.getStatusCode());

        Assert.assertEquals(0, userAccountResponse.getBody().books.size());

    }
}
