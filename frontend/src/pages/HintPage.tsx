import React, { FC } from 'react'
import 'scss/pages/_page_home.scss'
import { Page } from 'components/Page'
import { Content } from 'components/Content'
import { LargeHeaderContainer } from 'containers/LargeHeaderContainer'
import { HintContent } from 'components/HintContent'
import 'scss/pages/_page_hint.scss'

const HintPage: FC = () => (
  <Page className="page-hint">
    <LargeHeaderContainer title="Domov" />
    <Content className="content-home">
      <h2>Nápověda</h2>
      <HintContent />
    </Content>
  </Page>
)

export default HintPage
