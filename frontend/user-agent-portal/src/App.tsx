import { Navigate, Route, Routes } from 'react-router-dom';
import UserChatPage from './pages/UserChatPage.tsx';
import AgentDashboardPage from './pages/AgentDashboardPage.tsx';
import EmailSimulationPage from './pages/EmailSimulationPage.tsx';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/user" replace />} />
      <Route path="/user" element={<UserChatPage />} />
      <Route path="/agent" element={<AgentDashboardPage />} />
      <Route path="/email" element={<EmailSimulationPage />} />
      <Route path="*" element={<Navigate to="/user" replace />} />
    </Routes>
  );
}

export default App;
