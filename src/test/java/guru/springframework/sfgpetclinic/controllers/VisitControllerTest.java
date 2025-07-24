package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {
    @Mock
    VisitService visitService;
    @Spy //@Mock
    PetMapService petService;
    @InjectMocks
    VisitController visitController;

    @Test
    void loadPetWithVisit() {
        Map<String, Object> model = new HashMap<>();
        Pet pet21 = new Pet(21L);
        Pet pet83 = new Pet(83L);
        petService.save(pet21);
        petService.save(pet83);
        given(petService.findById(anyLong())).willCallRealMethod(); //.willReturn(pet);

        Visit visit = visitController.loadPetWithVisit(21L, model);

        assertThat(visit).isNotNull();
        assertThat(visit.getPet()).isNotNull();
        assertThat(visit.getPet().getId()).isEqualTo(21L);
        verify(petService, times(1)).findById(anyLong());
    }

    @Test
    void loadPetWithVisitWithStubbing() {
        Map<String, Object> model = new HashMap<>();
        Pet pet21 = new Pet(21L);
        Pet pet83 = new Pet(83L);
        petService.save(pet21);
        petService.save(pet83);
        given(petService.findById(anyLong())).willReturn(pet83);

        Visit visit = visitController.loadPetWithVisit(21L, model);

        assertThat(visit).isNotNull();
        assertThat(visit.getPet()).isNotNull();
        assertThat(visit.getPet().getId()).isEqualTo(83L);
        verify(petService, times(1)).findById(anyLong());
    }
}