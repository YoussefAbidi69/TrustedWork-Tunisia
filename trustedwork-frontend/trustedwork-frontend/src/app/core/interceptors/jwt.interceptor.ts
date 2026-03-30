import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Permissive check: any request to our known microservices (8081, 8083, etc.)
  const isApiRequest = req.url.includes(':808') || req.url.includes('/api/');

  if (token && isApiRequest) {
    console.log(`🔐 Attaching token to: ${req.url}`);
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(authReq);
  }
  
  return next(req);
};
