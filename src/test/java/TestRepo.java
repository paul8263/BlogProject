import com.paultech.config.SpringConfig;
import com.paultech.core.entities.*;
import com.paultech.core.services.AdministratorEntityService;
import com.paultech.core.services.BlogEntityService;
import com.paultech.core.services.UserBlogCommentService;
import com.paultech.core.services.UserEntityService;
import com.paultech.core.services.exceptions.UserNameConflictException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by paulzhang on 19/03/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class TestRepo {

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private BlogEntityService blogEntityService;

    @Autowired
    private UserBlogCommentService userBlogCommentService;

    @Autowired
    private AdministratorEntityService administratorEntityService;


    //Test object
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private BlogEntity blogEntity1;
    private BlogEntity blogEntity2;
    private UserBlogComment userBlogComment1;
    private UserBlogComment userBlogComment2;
    private AdministratorEntity administratorEntity;

    @Before
    public void setUp() {
        userEntity1 = new UserEntity();
        userEntity1.setUsername("Paul");
        userEntity1.setPassword("123456");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(1989,5,21);
        userEntity1.setBirthday(calendar1.getTime());
        userEntity1.setGender(Gender.MALE);
        userEntity1.setIconPath("/img/001.jpg");
        userEntity1.setRole("ROLE_USER");
        userEntity1.setSelfIntroduce("Hello Everyone");

        userEntity2 = new UserEntity();
        userEntity2.setUsername("Kate");
        userEntity2.setPassword("654321");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(1990,5,21);
        userEntity2.setBirthday(calendar2.getTime());
        userEntity2.setGender(Gender.FEMALE);
        userEntity2.setIconPath("/img/001.jpg");
        userEntity2.setRole("ROLE_USER");
        userEntity2.setSelfIntroduce("Hello Everyone");

        blogEntity1 = new BlogEntity();
        blogEntity1.setBlogId(1L);
        blogEntity1.setTitle("JavaScript");
        blogEntity1.setContent("Blog 1");
        blogEntity1.setCreateDate(new Date());
        blogEntity1.setModifyDate(new Date());
        blogEntity1.setOwner(userEntity1);

        blogEntity2 = new BlogEntity();
        blogEntity2.setBlogId(2L);
        blogEntity2.setTitle("Spring");
        blogEntity2.setContent("Blog 2");
        blogEntity2.setCreateDate(new Date());
        blogEntity2.setModifyDate(new Date());
        blogEntity2.setOwner(userEntity1);

        userBlogComment1 = new UserBlogComment();
        UserBlogCommentPK userBlogCommentPK1 = new UserBlogCommentPK();
        userBlogCommentPK1.setUserEntity(userEntity1);
        userBlogCommentPK1.setBlogEntity(blogEntity1);
        userBlogComment1.setUserBlogCommentPK(userBlogCommentPK1);
        userBlogComment1.setComment("User1 comment on Blog1");
        userBlogComment1.setCommentDate(new Date());

        userBlogComment2 = new UserBlogComment();
        UserBlogCommentPK userBlogCommentPK2 = new UserBlogCommentPK();
        userBlogCommentPK2.setUserEntity(userEntity1);
        userBlogCommentPK2.setBlogEntity(blogEntity2);
        userBlogComment2.setUserBlogCommentPK(userBlogCommentPK2);
        userBlogComment2.setComment("User1 comment on Blog2");
        userBlogComment2.setCommentDate(new Date());

        administratorEntity = new AdministratorEntity();
        administratorEntity.setUsername("Paul");
        administratorEntity.setPassword("666666");
    }

    @Test
    public void blank() {

    }

    @Test
    public void testUserServiceCreateAndFind() {

        userEntityService.createUser(userEntity1);

        UserEntity userEntity1 = userEntityService.findByUsername("Paul");
        System.out.println(userEntity1.getPassword());
    }

    @Test(expected = UserNameConflictException.class)
    public void testUserServiceDuplicated() {
        userEntityService.createUser(userEntity1);
        userEntityService.createUser(userEntity1);
    }

    @Test
    public void testUpdateUserEntity() {
        userEntityService.createUser(userEntity1);
        userEntity1.setUsername("Kate");
        userEntityService.updateUser(userEntity1);
    }

    @Test
    public void testDeleteUserEntity() {
        userEntityService.createUser(userEntity1);
        userEntityService.deleteUser(userEntity1);
    }

    @Test
    public void testAddBlogEntity() {
        userEntityService.createUser(userEntity1);
        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);
        List<BlogEntity> blogEntities = userEntityService.findByUsername("Paul").getBlogEntityList();
        for (BlogEntity blogEntity : blogEntities) {
            System.out.println(blogEntity.getContent());
        }
    }

    @Test
    public void testModifyBlogEntity() {
        userEntityService.createUser(userEntity1);
        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);
        List<BlogEntity> blogEntities = userEntityService.findByUsername("Paul").getBlogEntityList();
        for (BlogEntity blogEntity : blogEntities) {
            System.out.println(blogEntity.getContent());
        }
        System.out.println("-----------------");
        blogEntity2.setContent("Modified Blog 2");
        blogEntityService.save(blogEntity2);
        blogEntities = userEntityService.findByUsername("Paul").getBlogEntityList();
        for (BlogEntity blogEntity : blogEntities) {
            System.out.println(blogEntity.getContent());
        }
    }

    @Test
    public void testDeleteBlogEntity() {
        userEntityService.createUser(userEntity1);
        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);
        List<BlogEntity> blogEntities = userEntityService.findByUsername("Paul").getBlogEntityList();
        for (BlogEntity blogEntity : blogEntities) {
            System.out.println(blogEntity.getContent());
        }
        System.out.println("-----------------");
        blogEntityService.delete(blogEntity2);
        blogEntities = userEntityService.findByUsername("Paul").getBlogEntityList();
        for (BlogEntity blogEntity : blogEntities) {
            System.out.println(blogEntity.getContent());
        }
    }

    @Test
    public void testCommentCreation() {
        userEntityService.createUser(userEntity1);
        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);

        userBlogCommentService.save(userBlogComment1);
        userBlogCommentService.save(userBlogComment2);
    }

    @Test
    public void testCommentModification() {
        userEntityService.createUser(userEntity1);
        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);

        userBlogCommentService.save(userBlogComment1);
        userBlogCommentService.save(userBlogComment2);

        userBlogComment1.setComment("Modified comment1");

        userBlogCommentService.update(userBlogComment1);
    }

    @Test
    public void testCommentDeletion() {
        userEntityService.createUser(userEntity1);
        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);

        userBlogCommentService.save(userBlogComment1);
        userBlogCommentService.save(userBlogComment2);

        userBlogCommentService.delete(userBlogComment1);

    }


    @Test
    public void testAddAdministrator() {
        administratorEntityService.save(administratorEntity);
    }

    @Test
    public void testModifyAdministrator() {
        administratorEntityService.save(administratorEntity);
        administratorEntity.setPassword("qwertyuiop");
        administratorEntityService.save(administratorEntity);

    }

    @Test
    public void testDeleteAdministrator() {
        administratorEntityService.save(administratorEntity);
        administratorEntityService.delete(administratorEntity);
    }

    @Test
    public void testBlogEntityPage() {
        UserEntity userEntity = new UserEntity();
        userEntity = new UserEntity();
        userEntity.setUsername("Paul");
        userEntity.setPassword("123456");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(1989,5,21);
        userEntity.setBirthday(calendar1.getTime());
        userEntity.setGender(Gender.MALE);
        userEntity.setIconPath("/img/001.jpg");
        userEntity.setRole("ROLE_USER");
        userEntity.setSelfIntroduce("Hello Everyone");

        userEntityService.createUser(userEntity);

        for(int i = 0; i < 20; i++) {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity = new BlogEntity();
            blogEntity.setTitle("JavaScript");
            blogEntity.setContent("Blog 1" + i);
            blogEntity.setCreateDate(new Date());
            blogEntity.setModifyDate(new Date());
            blogEntity.setOwner(userEntity);
            blogEntityService.save(blogEntity);
        }

        Page<BlogEntity> blogEntities = blogEntityService.findAll(userEntity,new PageRequest(2,5));
        for (BlogEntity blogEntity : blogEntities) {
            System.out.println(blogEntity.getContent());
        }
    }


    @Test
    public void testCommentFindByBlogEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Paul");
        userEntity.setPassword("123456");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(1989,5,21);
        userEntity.setBirthday(calendar1.getTime());
        userEntity.setGender(Gender.MALE);
        userEntity.setIconPath("/img/001.jpg");
        userEntity.setRole("ROLE_USER");
        userEntity.setSelfIntroduce("Hello Everyone");

        userEntityService.createUser(userEntity);

        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle("JavaScript");
        blogEntity.setContent("Blog 1");
        blogEntity.setCreateDate(new Date());
        blogEntity.setModifyDate(new Date());
        blogEntity.setOwner(userEntity);
        blogEntityService.save(blogEntity);


        for(int i = 0; i < 20; i++) {
            UserEntity userEntity1 = new UserEntity();
            userEntity1.setUsername("Paul" + i);
            userEntity1.setPassword("123456");
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(1989,5,21);
            userEntity1.setBirthday(calendar1.getTime());
            userEntity1.setGender(Gender.MALE);
            userEntity1.setIconPath("/img/001.jpg");
            userEntity1.setRole("ROLE_USER");
            userEntity1.setSelfIntroduce("Hello Everyone");
            userEntityService.createUser(userEntity1);

            UserBlogComment userBlogComment = new UserBlogComment();
            UserBlogCommentPK userBlogCommentPK1 = new UserBlogCommentPK();
            userBlogCommentPK1.setUserEntity(userEntity1);
            userBlogCommentPK1.setBlogEntity(blogEntity);
            userBlogComment.setUserBlogCommentPK(userBlogCommentPK1);
            userBlogComment.setComment("User" + i + " comment on Blog1");
            userBlogComment.setCommentDate(new Date());

            userBlogCommentService.save(userBlogComment);

        }

        System.out.println("----------------");

        Page<UserBlogComment> userBlogComments = userBlogCommentService.findByBlogEntity(blogEntityService.findById(1L),new PageRequest(1,5));
        for(UserBlogComment userBlogComment : userBlogComments) {
            System.out.println(userBlogComment.getUserBlogCommentPK().getUserEntity().getUsername());
        }


    }

    @Test
    public void testCommentFindByUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Paul");
        userEntity.setPassword("123456");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(1989,5,21);
        userEntity.setBirthday(calendar1.getTime());
        userEntity.setGender(Gender.MALE);
        userEntity.setIconPath("/img/001.jpg");
        userEntity.setRole("ROLE_USER");
        userEntity.setSelfIntroduce("Hello Everyone");
        userEntityService.createUser(userEntity);

        for(int i = 0; i < 20; i++) {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setTitle("JavaScript");
            blogEntity.setContent("Blog " + i);
            blogEntity.setCreateDate(new Date());
            blogEntity.setModifyDate(new Date());
            blogEntity.setOwner(userEntity);
            blogEntityService.save(blogEntity);

            UserBlogComment userBlogComment = new UserBlogComment();
            UserBlogCommentPK userBlogCommentPK1 = new UserBlogCommentPK();
            userBlogCommentPK1.setUserEntity(userEntity);
            userBlogCommentPK1.setBlogEntity(blogEntity);
            userBlogComment.setUserBlogCommentPK(userBlogCommentPK1);
            userBlogComment.setComment("User" + " comment on Blog" + i);
            userBlogComment.setCommentDate(new Date());

            userBlogCommentService.save(userBlogComment);

        }

        System.out.println("-------------");
        Page<UserBlogComment> userBlogComments = userBlogCommentService.findByUserEntity(userEntityService.findByUsername("Paul"), new PageRequest(1,5));
        for(UserBlogComment userBlogComment : userBlogComments) {
            System.out.println(userBlogComment.getComment());
        }
    }

    @Test
    public void testGetUserBlogComment() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Paul");
        userEntity.setPassword("123456");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(1989,5,21);
        userEntity.setBirthday(calendar1.getTime());
        userEntity.setGender(Gender.MALE);
        userEntity.setIconPath("/img/001.jpg");
        userEntity.setRole("ROLE_USER");
        userEntity.setSelfIntroduce("Hello Everyone");
        userEntityService.createUser(userEntity);

        for(int i = 0; i < 20; i++) {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setTitle("JavaScript");
            blogEntity.setContent("Blog " + i);
            blogEntity.setCreateDate(new Date());
            blogEntity.setModifyDate(new Date());
            blogEntity.setOwner(userEntity);
            blogEntityService.save(blogEntity);

            UserBlogComment userBlogComment = new UserBlogComment();
            UserBlogCommentPK userBlogCommentPK1 = new UserBlogCommentPK();
            userBlogCommentPK1.setUserEntity(userEntity);
            userBlogCommentPK1.setBlogEntity(blogEntity);
            userBlogComment.setUserBlogCommentPK(userBlogCommentPK1);
            userBlogComment.setComment("User" + " comment on Blog" + i);
            userBlogComment.setCommentDate(new Date());

            userBlogCommentService.save(userBlogComment);

        }

        UserBlogCommentPK pk = new UserBlogCommentPK();
        pk.setUserEntity(userEntityService.findByUsername("Paul"));
        pk.setBlogEntity(blogEntityService.findById(2L));
        UserBlogComment userBlogComment = userBlogCommentService.findById(pk);
        System.out.println("-------------------");
        System.out.println(userBlogComment.getComment());

    }

    @Test
    public void prepareUser() {
        userEntityService.createUser(userEntity1);
        userEntityService.createUser(userEntity2);


        blogEntityService.save(blogEntity1);
        blogEntityService.save(blogEntity2);

    }

}
