import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const PASSWORD_MIN_LENGTH = 8;

export const strongPasswordValidator = (): ValidatorFn => {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value ?? '') as string;
    if (!value) {
      return null;
    }

    const errors: ValidationErrors = {};
    if (!/[a-z]/.test(value)) errors['passwordLower'] = true;
    if (!/[A-Z]/.test(value)) errors['passwordUpper'] = true;
    if (!/\d/.test(value)) errors['passwordDigit'] = true;
    if (!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(value)) errors['passwordSpecial'] = true;
    return Object.keys(errors).length ? errors : null;
  };
};

export const passwordStrength = (value: string): 0 | 1 | 2 | 3 | 4 => {
  if (!value) return 0;
  let score = 0;
  if (value.length >= PASSWORD_MIN_LENGTH) score++;
  if (/[a-z]/.test(value) && /[A-Z]/.test(value)) score++;
  if (/\d/.test(value)) score++;
  if (/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(value)) score++;
  return Math.min(score, 4) as 0 | 1 | 2 | 3 | 4;
};
