// src/components/toStylePx/index.tsx
import React, { createContext, useContext } from 'react';

const ViewportContext = createContext({});
export const ViewportProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return <ViewportContext.Provider value={{}}>{children}</ViewportContext.Provider>;
};
export const useViewport = () => useContext(ViewportContext);
