export const environment = {
  production: false,
  api: {
    userBaseUrl: 'http://localhost:8080/api',
    // Keep jobs on same-origin /api path (same pattern as working frontend project).
    jobBaseUrl: '/api'
  }
};
