package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository repository;

    private User user;
    private BlogPost post;
    private LikePost like;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(user);
        post = new BlogPost();
        post.setUser(user);
        post.setEntry("test");
        entityManager.persist(post);
        like = new LikePost();
        like.setUser(user);
        like.setPost(post);
    }

    @Test
    public void Check_repository_is_returning_actual_post_after_updating_this() {
        repository.save(like);
        User newUser = new User();
        newUser.setFirstName("Test");
        newUser.setLastName("Test");
        newUser.setEmail("Test");
        newUser.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(newUser);
        like.setUser(newUser);
        repository.save(like);

        List<LikePost> likes = repository.findAll();
        LikePost foundLike = likes.get(0);

        Assert.assertThat(foundLike.getUser(), Matchers.equalTo(newUser));
    }

    @Test
    public void Check_repository_can_found_existing_like() {
        repository.save(like);

        LikePost foundLike = repository.findByUserAndPost(user, post).orElse(null);

        Assert.assertThat(foundLike, Matchers.is(like));
    }
    @Test
    public void Check_repository_is_returning_post_after_post_was_added() {
        repository.save(like);

        List<LikePost> likes = repository.findAll();

        Assert.assertThat(likes, Matchers.contains(like));
    }

    @Test
    public void Check_repository_is_returning_empty_list_of_posts_after_deleting() {
        repository.save(like);
        repository.delete(like);

        List<LikePost> likes = repository.findAll();

        Assert.assertThat(likes, Matchers.not(Matchers.contains(like)));
    }
}