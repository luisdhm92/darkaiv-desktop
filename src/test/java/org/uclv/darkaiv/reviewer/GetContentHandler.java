/////*
//// * To change this license header, choose License Headers in Project Properties.
//// * To change this template file, choose Tools | Templates
//// * and open the template in the editor.
//// */
////package org.uclv.darkaiv.reviewer;
////
////import java.io.IOException;
////import javax.servlet.ServletException;
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
////import org.eclipse.jetty.server.Request;
////import org.eclipse.jetty.server.handler.AbstractHandler;
////
/////**
//// *
//// * @author daniel
//// */
////public class GetContentHandler extends AbstractHandler {
////
////    @Override
////    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
////            throws IOException, ServletException {
////        response.setContentType("text/html;charset=utf-8");
////        response.setStatus(HttpServletResponse.SC_OK);
////        baseRequest.setHandled(true);
////        response.getWriter().println("It works");
////    }
////}
