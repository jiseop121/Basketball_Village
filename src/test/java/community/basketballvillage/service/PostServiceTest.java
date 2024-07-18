package community.basketballvillage.service;

import static community.basketballvillage.dummy.DummyObject.newUserForTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;

import community.basketballvillage.domain.Post;
import community.basketballvillage.domain.PostLike;
import community.basketballvillage.domain.Reply;
import community.basketballvillage.domain.User;
import community.basketballvillage.dto.request.PostDto;
import community.basketballvillage.dto.response.ResPostDto;
import community.basketballvillage.global.exception.BusinessException;
import community.basketballvillage.repository.PostLikeRepository;
import community.basketballvillage.repository.PostRepository;
import community.basketballvillage.repository.ReplyRepository;
import community.basketballvillage.repository.UserRepository;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private static final String EMAIL = "admin123@naver.com";
    private static final String TEST_CONTENT = "test content";
    private static final String TEST_TITLE = "test title";
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_POST_ID = 1L;

    private static final User TEST_USER = newUserForTest(EMAIL);
    private static Post testPost;
    private static List<Reply> replies;

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReplyRepository replyRepository;

    @BeforeEach
    void reset(){
        testPost = new Post(
            TEST_USER,
            TEST_TITLE,
            TEST_CONTENT);
        Reply reply = new Reply(
            1L,
            TEST_USER,
            testPost,
            "test reply content",
            LocalDateTime.now(),
            LocalDateTime.now(),
            0
        );
        replies = List.of(reply);
    }

    @Test
    void 정상_Post_등록_후_allPostByUser() {
        //given
        List<Post> allPostsByUser = List.of(testPost);

        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findAllByUser(TEST_USER)).willReturn(allPostsByUser);

        //when
        List<ResPostDto> resPostDtos = postService.allPostByUser(TEST_USER_ID);

        //then
        verify(postRepository).findAllByUser(TEST_USER);
        verify(userRepository).findById(TEST_USER_ID);
        assertThat(resPostDtos.size()).isEqualTo(1);
        assertThat(resPostDtos.get(0).getTitle()).isEqualTo(TEST_TITLE);
        assertThat(resPostDtos.get(0).getContent()).isEqualTo(TEST_CONTENT);
        assertThat(resPostDtos.get(0).getModifiedAt()).isNull();
        assertThat(resPostDtos.get(0).getViewCnt()).isZero();
        assertThat(resPostDtos.get(0).getLikeCnt()).isZero();
    }

    @Test
    void 정상_detailPost_viewCnt_증가(){
        //given
        given(postRepository.findById(1L)).willReturn(Optional.ofNullable(testPost));
        given(replyRepository.findAllByPost(testPost)).willReturn(null);
        //when
        int repeatCnt = 5;
        for(int i=0;i<repeatCnt;i++){
            ResPostDto resPostDto = postService.detailPost(TEST_POST_ID);
            //then
            assertThat(resPostDto.getViewCnt()).isEqualTo(i+1);
        }
    }

    @Test
    void 정상_updatePost_title_content_모두수정() {
        //given
        String updateTitle = "test title update";
        String updateContent = "test content update";
        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findByIdAndUser(TEST_POST_ID,TEST_USER)).willReturn(Optional.of(testPost));

        //when
        ResPostDto resPostDto = postService.updatePost(TEST_USER_ID, TEST_POST_ID,
            new PostDto(updateTitle, updateContent));

        //then
        assertThat(resPostDto.getTitle()).isEqualTo(updateTitle);
        assertThat(resPostDto.getContent()).isEqualTo(updateContent);
        verify(postRepository).findByIdAndUser(TEST_POST_ID, TEST_USER);
    }

    @Test
    void 정상_updatePost_title_수정() {
        //given
        String updateTitle = "test title update";
        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findByIdAndUser(TEST_POST_ID,TEST_USER)).willReturn(Optional.of(testPost));
        given(replyRepository.findAllByPost(testPost)).willReturn(null);

        //when
        ResPostDto resPostDto = postService.updatePost(TEST_USER_ID, TEST_POST_ID,
            new PostDto(updateTitle, null));

        //then
        assertThat(resPostDto.getTitle()).isEqualTo(updateTitle);
        assertThat(resPostDto.getContent()).isEqualTo(testPost.getContent());
        verify(postRepository,times(1)).findByIdAndUser(1L, TEST_USER);
    }

    @Test
    void 정상_updatePost_content_수정() {
        //given
        String updateContent = "test content update";
        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findByIdAndUser(TEST_POST_ID,TEST_USER)).willReturn(Optional.of(testPost));
        given(replyRepository.findAllByPost(testPost)).willReturn(null);

        //when
        ResPostDto resPostDto = postService.updatePost(TEST_USER_ID, TEST_POST_ID,
            new PostDto(null, updateContent));

        //then
        assertThat(resPostDto.getTitle()).isEqualTo(testPost.getTitle());
        assertThat(resPostDto.getContent()).isEqualTo(updateContent);
        verify(postRepository,times(1)).findByIdAndUser(1L, TEST_USER);
    }

    @Test
    void 정상_likePost_좋아요추가(){
        PostLike postLike = getPostLike();
        //given
        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));
        given(postLikeRepository.findByUserAndPost(TEST_USER,testPost)).willReturn(Optional.ofNullable(null));
        given(postLikeRepository.save(any(PostLike.class))).willReturn(postLike);

        //when
        assertThat(testPost.getLikeCnt()).isEqualTo(0);
        postService.likePost(TEST_USER_ID,TEST_POST_ID);

        //then
        assertThat(testPost.getLikeCnt()).isEqualTo(1);
        verify(postLikeRepository).save(any(PostLike.class));
        verify(postLikeRepository,never()).delete(any());
    }



    @Test
    void 정상_likePost_좋아요가_있는경우_삭제(){
        PostLike postLike = getPostLike();
        //given
        testPost.increaseLikeCnt();
        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(testPost));
        given(postLikeRepository.findByUserAndPost(TEST_USER,testPost)).willReturn(Optional.of(postLike));

        doNothing().when(postLikeRepository).delete(postLike);

        //when
        assertThat(testPost.getLikeCnt()).isEqualTo(1);
        postService.likePost(TEST_USER_ID,TEST_POST_ID);

        //then
        assertThat(testPost.getLikeCnt()).isEqualTo(0);
        verify(postLikeRepository).delete(postLike);
        verify(postLikeRepository,never()).save(postLike);
    }

    @Test
    void 비정상_없는유저_BusinessException_호출(){
        //given\
        given(userRepository.findById(2L)).willThrow(BusinessException.class);

        //when,then
        assertThatThrownBy(() -> postService.updatePost(2L,1L,new PostDto()))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> postService.allPostByUser(2L))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> postService.addPost(2L,new PostDto()))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> postService.deletePost(2L,1L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    void 비정상_없는post_BusinessException_호출(){

        //given
        given(userRepository.findById(TEST_USER_ID)).willReturn(Optional.of(TEST_USER));
        given(postRepository.findByIdAndUser(2L,TEST_USER)).willThrow(BusinessException.class);
        given(postRepository.findById(2L)).willThrow(BusinessException.class);

        //when,then
        assertThatThrownBy(() -> postService.detailPost(2L))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> postService.deletePost(TEST_USER_ID,2L))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> postService.updatePost(TEST_USER_ID,2L,new PostDto()))
            .isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> postService.likePost(TEST_USER_ID,2L))
            .isInstanceOf(BusinessException.class);
    }

    private static PostLike getPostLike() {
        return PostLike.builder()
            .user(TEST_USER)
            .post(testPost)
            .build();
    }
}