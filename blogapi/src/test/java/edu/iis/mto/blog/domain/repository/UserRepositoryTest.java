package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setLastName("Nowak");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void Check_Repository_is_finding_user_when_good_firstname_provided(){
        User result = entityManager.persist(user);
        List<User> repositoryResult = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                result.getFirstName(),
                "sss",
                "sss");
        Assert.assertThat(repositoryResult, Matchers.contains(result));
    }

    @Test
    public void Check_Repository_is_finding_user_when_good_email_provided(){
        User result = entityManager.persist(user);
        List<User> repositoryResult = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "sss",
                "sss",
                result.getEmail());
        Assert.assertThat(repositoryResult, Matchers.contains(result));
    }

    @Test
    public void Check_Repository_is_finding_user_when_good_lastname_provided(){
        User result = entityManager.persist(user);
        List<User> repositoryResult = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "sss",
                result.getLastName(),
                "sss");
        Assert.assertThat(repositoryResult, Matchers.contains(result));
    }

    @Test
    public void Check_Repository_is_not_finding_user_when_bad_information_provided(){
        List<User> repositoryResult = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(
                "sss",
                "sss",
                "sss");
        Assert.assertThat(repositoryResult, Matchers.hasSize(0));
    }

}
