package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @MockBean
    private BlogPostRepository postRepository;

    @MockBean
    private LikePostRepository likeRepository;

    private User conUser;
    private User uConUser;
    private BlogPost post;

    @Before
    public void setUp() {
        conUser = new User();
        conUser.setId(1L);
        conUser.setFirstName("Jan");
        conUser.setLastName("Nowak");
        conUser.setEmail("john@domain.com");
        conUser.setAccountStatus(AccountStatus.CONFIRMED);

        uConUser = new User();
        uConUser.setId(2L);
        uConUser.setFirstName("Test");
        uConUser.setLastName("Test");
        uConUser.setEmail("Test");
        uConUser.setAccountStatus(AccountStatus.NEW);

        post = new BlogPost();
        post.setId(3L);
        post.setUser(uConUser);
        post.setEntry("");

        Mockito.when(userRepository.findById(uConUser.getId()))
                .thenReturn(Optional.of(uConUser));
        Mockito.when(userRepository.findById(conUser.getId()))
                .thenReturn(Optional.of(conUser));
        Mockito.when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void Check_BlogService_is_not_throwing_error_when_confirmed_user_like_post() {
        blogService.addLikeToPost(conUser.getId(), post.getId());
        Assert.assertTrue(true);
    }

    @Test(expected = DomainError.class)
    public void Check_BlogService_is_throwing_error_when_unconfirmed_user_like_post(){
        blogService.addLikeToPost(uConUser.getId(), post.getId());
        Assert.assertTrue(true);
    }

}
