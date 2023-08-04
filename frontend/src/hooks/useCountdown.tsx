import { useState, useEffect, useCallback } from 'react';

export const useCountdown =(
  timeout: number,
  initData = false
): {
  isCountdownActivate: boolean
  activate: () => void
  deactivate: () => void
} => {
  const [countdownState, setCountdownState] = useState(initData);

  const activate = useCallback(() => {
    setCountdownState(true);
  }, []);

  const deactivate = useCallback(() => {
    setCountdownState(false);
  }, []);

  useEffect(
    () => {
      if (!countdownState) {
        return;
      }

      const handler = setTimeout((): void => {
        deactivate();
      }, timeout);

      return (): void => {
        clearTimeout(handler);
      };
    },
    [countdownState, timeout, deactivate]
  );

  return { isCountdownActivate: countdownState, activate, deactivate };
};
