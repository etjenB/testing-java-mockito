package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {
    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";
    @Mock(lenient = true)
    OwnerService ownerService;
    @InjectMocks
    OwnerController controller;
    @Mock
    BindingResult bindingResult;
    @Mock
    Model model;
    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willAnswer(invocationOnMock -> {
           List<Owner> owners = new ArrayList<>();
           String name = invocationOnMock.getArgument(0);
           if (name.equals("%Buck%")) {
               owners.add(new Owner(1L, "Joe", "Buck"));
               return owners;
           } else if (name.equals("%NNNNN%")) {
               return owners;
           } else if (name.equals("%M%")) {
               owners.add(new Owner(1L, "Joe", "M"));
               owners.add(new Owner(2L, "Franc", "Ma"));
               return owners;
           }
           throw new RuntimeException("Invalid argument");
        });
    }

    @Test
    void processFindFormWildcardStringAnnotation() {
        Owner owner = new Owner(1L, "Joe", "Buck");
        String viewName = controller.processFindForm(owner, bindingResult, null);
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("%Buck%");
        assertThat(viewName).isEqualTo("redirect:/owners/1");
        verifyNoInteractions(model);
    }

    @Test
    void processFindFormWildcardStringAnnotationNotFound() {
        Owner owner = new Owner(1L, "Joe", "NNNNN");
        String viewName = controller.processFindForm(owner, bindingResult, null);
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("%NNNNN%");
        assertThat(viewName).isEqualTo("owners/findOwners");
        verifyNoInteractions(model);
    }

    @Test
    void processFindFormWildcardStringAnnotationFoundMultiple() {
        Owner owner = new Owner(1L, "Joe", "M");
        InOrder inOrder = Mockito.inOrder(ownerService, model);
        String viewName = controller.processFindForm(owner, bindingResult, model);
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("%M%");
        assertThat(viewName).isEqualTo("owners/ownersList");
        //verify order of calls
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model, times(1)).addAttribute(anyString(), anyList());
    }

    @Test
    void processFindFormWildcardString() {
        Owner owner = new Owner(1L, "Joe", "Buck");
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        controller.processFindForm(owner, bindingResult, null);
        verify(ownerService).findAllByLastNameLike(captor.capture());
        assertThat(captor.getValue()).isEqualTo("%Buck%");
    }

    @Test
    void processCreationFormHasErrors() {
        //given
        Owner owner = new Owner(1l, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormNoErrors() {
        //given
        Owner owner = new Owner(5l, "Jim", "Bob");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        //when
        String viewName = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewName).isEqualToIgnoringCase(REDIRECT_OWNERS_5);
    }
}