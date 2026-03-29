import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Keep auth endpoints public and avoid attaching stale tokens to login/register flows.
  const isPublicAuthRequest = /\/auth\/(login|register|forgot-password|reset-password|refresh)$/.test(req.url);
  if (isPublicAuthRequest) {
    return next(req);
  }

  const token = inject(AuthService).getToken();
  if (!token) {
    return next(req);
  }
  return next(
    req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    })
  );
};
