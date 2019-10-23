import React, { FC } from 'react'
import { RouteComponentProps } from 'react-router-dom'
import 'scss/pages/_page_description.scss'
import { Page } from 'components/Page'
import { Content } from 'components/Content'
import DescriptionContainer from 'containers/DescriptionContainer'
import { SmallHeader } from '../components/SmallHeader'

function utilizeURL(url: string): string {
  let finalURL = url.substr(1)

  if (url.slice(-1) === '/') {
    finalURL = finalURL.slice(0, -1)
  }

  return finalURL
}

const DescriptionPage: FC<RouteComponentProps<{ url: string }>> = ({
  location: { pathname }
}) => (
  <Page>
    <SmallHeader />
    <Content fluid className="content-description-page">
      <DescriptionContainer pageUrl={utilizeURL(pathname)} />
    </Content>
  </Page>
)
export default DescriptionPage
