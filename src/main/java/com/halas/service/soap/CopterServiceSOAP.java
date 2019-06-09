package com.halas.service.soap;

import com.halas.exeption.DuplicateCopterIdException;
import com.halas.exeption.MaximumDistanceExceededException;
import com.halas.exeption.NoSuchCopterIdException;
import com.halas.model.Copter;
import com.halas.model.Position;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface CopterServiceSOAP {
    @WebMethod
    boolean addCopter(Copter copter) throws DuplicateCopterIdException;

    @WebMethod
    List<Copter> showAllCopters();

    @WebMethod
    boolean deleteCopterById(int id) throws NoSuchCopterIdException;

    @WebMethod
    boolean moveToPositionById(int id, Position newPosition) throws MaximumDistanceExceededException, NoSuchCopterIdException;

    @WebMethod
    boolean goUp(int idCopter) throws MaximumDistanceExceededException, NoSuchCopterIdException;

    @WebMethod
    boolean goDown(int idCopter) throws MaximumDistanceExceededException, NoSuchCopterIdException;

    @WebMethod
    boolean goByDegree(int idCopter, double degree) throws MaximumDistanceExceededException, NoSuchCopterIdException;

    @WebMethod
    boolean holdPosition(int idCopter) throws NoSuchCopterIdException;
}
