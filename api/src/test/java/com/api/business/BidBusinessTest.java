package com.api.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.dao.BidDao;
import com.api.data.entity.Bid;
import com.api.exception.business.NotFoundException;
import com.api.exception.business.ResourceStateConflictException;
import com.api.exception.technical.DAOException;

@ExtendWith(MockitoExtension.class)
public class BidBusinessTest {

    @Mock
    private BidDao bidDao;

    @InjectMocks
    private BidBusiness bidBusiness;

    private Bid bid1;
    private Bid bid2;

    @BeforeEach
    public void setUp() {
        // Initialisation de quelques instances de Bid pour les tests
        bid1 = new Bid();
        bid1.setId(1L);
        bid1.setBid(100);
        bid1.setMessage("Premier bid");

        bid2 = new Bid();
        bid2.setId(2L);
        bid2.setBid(200);
        bid2.setMessage("Deuxième bid");
    }

    @Test
    public void testGetAll() throws DAOException {
        List<Bid> bidList = Arrays.asList(bid1, bid2);
        when(bidDao.getAll()).thenReturn(bidList);

        List<Bid> result = bidBusiness.getAll();

        assertEquals(bidList, result);
        verify(bidDao, times(1)).getAll();
    }

    @Test
    public void testGetSuccess() throws DAOException, NotFoundException {
        when(bidDao.get(1L)).thenReturn(bid1);

        Bid result = bidBusiness.get(1L);

        assertEquals(bid1, result);
        verify(bidDao, times(1)).get(1L);
    }

    @Test
    public void testGetNotFound() throws DAOException {
        when(bidDao.get(3L)).thenReturn(null);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bidBusiness.get(3L);
        });

        assertEquals("The Bid with id 3 does not exist", thrown.getMessage());
        verify(bidDao, times(1)).get(3L);
    }

    @Test
    public void testAdd() throws DAOException, NotFoundException {
        Bid newBid = new Bid();
        newBid.setId(3L);
        newBid.setBid(300);
        newBid.setMessage("Nouveau bid");

        // Simulation de la méthode create (void)
        doNothing().when(bidDao).create(newBid);
        // Après création, le DAO doit renvoyer le nouveau Bid lors d'un get
        when(bidDao.get(3L)).thenReturn(newBid);

        Bid result = bidBusiness.add(newBid);

        assertEquals(newBid, result);
        verify(bidDao, times(1)).create(newBid);
        verify(bidDao, times(1)).get(3L);
    }

    @Test
    public void testUpdateSuccess() throws DAOException, NotFoundException {
        long idToUpdate = 1L;
        // Simulation de la récupération de l'entité existante
        when(bidDao.get(idToUpdate)).thenReturn(bid1);

        // Création d'un Bid avec les nouvelles valeurs (sans id, l'id reste celui de l'entité existante)
        Bid updatedBid = new Bid();
        updatedBid.setBid(150);
        updatedBid.setMessage("Updated message");

        // Simulation de la mise à jour dans le DAO
        doNothing().when(bidDao).update(bid1);
        // Après mise à jour, on simule que le DAO renvoie l'entité modifiée
        when(bidDao.get(idToUpdate)).thenReturn(bid1);

        Bid result = bidBusiness.update(idToUpdate, updatedBid);

        // Vérifier que les champs ont bien été mis à jour
        assertEquals(150, bid1.getBid());
        assertEquals("Updated message", bid1.getMessage());
        assertNotNull(bid1.getDateUpdate());
        // Le résultat doit correspondre à l'entité mise à jour
        assertEquals(bid1, result);

        // Vérifier les appels sur le DAO :
        // Une première fois dans get() puis une seconde fois après update()
        verify(bidDao, times(2)).get(idToUpdate);
        verify(bidDao, times(1)).update(bid1);
    }

    @Test
    public void testUpdateNotFound() throws DAOException {
        long nonExistentId = 10L;
        when(bidDao.get(nonExistentId)).thenReturn(null);

        Bid updatedBid = new Bid();
        updatedBid.setBid(150);
        updatedBid.setMessage("Updated message");

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bidBusiness.update(nonExistentId, updatedBid);
        });

        assertEquals("The Bid with id 10 does not exist", thrown.getMessage());
        verify(bidDao, times(1)).get(nonExistentId);
        verify(bidDao, never()).update(any(Bid.class));
    }

    @Test
    public void testDeleteSuccess() throws DAOException, ResourceStateConflictException {
        long idToDelete = 1L;
        when(bidDao.get(idToDelete)).thenReturn(bid1);
        doNothing().when(bidDao).delete(bid1);

        bidBusiness.delete(idToDelete);

        verify(bidDao, times(1)).get(idToDelete);
        verify(bidDao, times(1)).delete(bid1);
    }
}
